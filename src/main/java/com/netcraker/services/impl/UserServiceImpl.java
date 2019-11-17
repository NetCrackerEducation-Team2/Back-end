package com.netcraker.services.impl;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.repositories.AuthorizationRepository;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.repositories.UserRoleRepository;
import com.netcraker.services.EmailSenderService;
import com.netcraker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@PropertySource("classpath:email-messages.properties")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorizationRepository authorizationRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSender;

    @Override
    public User createUsualUser(User user) {
        final User registered = createUser(user);
        final AuthorizationLinks authorizationLink = authorizationRepository.creteAuthorizationLinks(registered);
        emailSender.sendActivationCode(user, authorizationLink);
        return registered;
    }

    @Override
    public boolean activateUser(String token) {
        AuthorizationLinks authorizationLinks;
        try {
            authorizationLinks = authorizationRepository.findByActivationCode(token);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }

        if (authorizationLinks == null || authorizationLinks.isUsed()) {
            return false;
        }
        System.out.println("Auth link has user's id:" + authorizationLinks.getUserId());
        Optional<User> userOpt = userRepository.getById(authorizationLinks.getUserId());

        if (!userOpt.isPresent()) {
            return false;
        }
        User user = userOpt.get();
        authorizationLinks.setUsed(true);
        user.setEnabled(true);
        userRepository.update(user);
        authorizationRepository.updateAuthorizationLinks(authorizationLinks);
        return true;
    }

    @Override
    public User createAdminModerator(User user, Role role) {
        final User registered = createUser(user);
        final Role userRole = roleRepository.findByName(role.getName());
        userRoleRepository.createUserRole(registered, userRole);
        return user;
    }

    private User createUser(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isPresent()) {
            throw new FailedToRegisterException("Email is already used");
        }
        //for hashing
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        final User registered = userRepository.insert(user)
                .orElseThrow(() -> new FailedToRegisterException("Error in creating user! Email is free, but creation query failure."));

        System.out.println("user is created with id: " + registered.getUserId());

        return registered;
    }


    @Override
    public User findByUserId(int userId) {
        return userRepository.getById(userId).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void updateUser(User oldUser, User newUser) {
        System.out.println(newUser + " to update");
        userRepository.update(newUser);
    }

    @Override
    public void updateUserRole(Role oldRole, User newUser) {
        userRoleRepository.updateUserRole(oldRole, newUser);
    }

    @Override
    public boolean equalsPassword(User user, String rawPassword) {
        System.out.println("Old password: " + user.getPassword() + " new password: " + rawPassword);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public User changePassword(int userId, String oldPass, String newPass) {
        final User user = this.userRepository.getById(userId)
                .orElseThrow(() -> new FindException("Cannot find user with such id"));

        if (!equalsPassword(user, oldPass))
            throw new UpdateException("Wrong entered old password");

        user.setPassword(passwordEncoder.encode(newPass));

        return userRepository.update(user)
                .orElseThrow(() -> new UpdateException("Cannot update password"));
    }
}