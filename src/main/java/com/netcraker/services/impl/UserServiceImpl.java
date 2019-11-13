package com.netcraker.services.impl;

import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.repositories.AuthorizationRepository;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.repositories.UserRoleRepository;
import com.netcraker.security.SecurityConstants;
import com.netcraker.services.MailSender;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final @NonNull UserRepository userRepository;
    private final @NonNull AuthorizationRepository authorizationRepository;
    private final @NonNull RoleRepository roleRepository;
    private final @NonNull UserRoleRepository userRoleRepository;
    private final MailSender mailSender;

    @Override
    public User createUser(User user) {
        User userFromDB = null;
        try {
            userFromDB = userRepository.findByEmail(user.getEmail());
            if (userFromDB != null) {
                throw new FailedToRegisterException("Email is already used");
            }
        } catch (EmptyResultDataAccessException e){

        }

        final User registered = userRepository.createUser(user);
        System.out.println("created with id: " + registered.getUserId());

        AuthorizationLinks authorizationLinks = new AuthorizationLinks();
        authorizationLinks.setToken(UUID.randomUUID().toString());
        authorizationLinks.setUserId(userRepository.findByEmail(registered.getEmail()).getUserId());
        authorizationLinks.setRegistrationToken(true);
        authorizationLinks.setUsed(false);
        authorizationRepository.creteAuthorizationLinks(authorizationLinks);
        
        mailSender.send(user, "Activation code", authorizationLinks);
        return user;
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

        if (authorizationLinks == null) {
            return false;
        }
        System.out.println("Auth link has user's id:" + authorizationLinks.getUserId());
        User user = userRepository.findByUserId(authorizationLinks.getUserId());

        if (user == null) {
            return false;
        }
        authorizationLinks.setUsed(true);
        user.setEnabled(true);
        userRepository.updateUser(user);
        authorizationRepository.updateAuthorizationLinks(authorizationLinks);
        return true;
    }

    @Override
    public User createAdminModerator(User user, Role role){
        User userFromDB = null;
        try {
            roleRepository.findByName(role.getName());
            userFromDB = userRepository.findByEmail(user.getEmail());

        } catch (DataAccessException ignored) {

        }
        if (userFromDB != null) {
            throw new FailedToRegisterException("Email is already used");
        }

        final User registered = userRepository.createUser(user);
        final Role userRole = roleRepository.findByName(role.getName());
        userRoleRepository.createUserRole(registered,userRole);
        return user;
    }

    @Override
    public User findByUserId(int userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUser(User oldUser, User newUser) {
        userRepository.updateUser(oldUser, newUser);
    }

    @Override
    public void updateUserRole(Role oldRole, User newUser) {
        userRoleRepository.updateUserRole(oldRole, newUser);
    }

//    @Override
//    public void deleteAdminModerator(User user) {
//    }
}