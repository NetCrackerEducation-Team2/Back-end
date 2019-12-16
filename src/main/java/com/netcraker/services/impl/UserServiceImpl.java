package com.netcraker.services.impl;

import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.NoUserRoleProvided;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.*;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.repositories.impl.AuthorizationRepositoryImpl;
import com.netcraker.repositories.impl.UserRoleRepositoryImpl;
import com.netcraker.services.AuthEmailSenderService;
import com.netcraker.services.PageService;
import com.netcraker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@PropertySource("classpath:email-messages.properties")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PageService pageService;
    private final UserRepository userRepository;
    private final AuthorizationRepositoryImpl authorizationRepositoryImpl;
    private final RoleRepository roleRepository;
    private final UserRoleRepositoryImpl userRoleRepositoryImpl;
    private final PasswordEncoder passwordEncoder;
    private final AuthEmailSenderService emailSender;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User createUsualUser(User user) {
        Role USER_ROLE = roleRepository.findByName("USER").orElseThrow(InternalError::new);
        user.setEnabled(false);
        user.setRoles(Collections.singletonList(USER_ROLE));
        final User registered = createUser(user);
        final AuthorizationLinks authorizationLink = authorizationRepositoryImpl.creteAuthorizationLinks(registered);
        emailSender.sendActivationCode(user, authorizationLink);
        return registered;
    }

    @Override
    public User createAdminModerator(User user) {
        user.setEnabled(true);
        List<Role> setRoles = new ArrayList<>();
        if(user.getRoles().isEmpty()) {
            throw new FindException("Role not found");
        }
        for (Role role : user.getRoles()) {
            Optional<Role> roleFromDB = roleRepository.findByName(role.getName());
            setRoles.add(roleFromDB.get());
        }
        user.setRoles(setRoles);
        return createUser(user);
    }

    @Override
    public boolean activateUser(String token) {
        AuthorizationLinks authorizationLinks;
        try {
            authorizationLinks = authorizationRepositoryImpl.findByActivationCode(token);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }

        if (authorizationLinks == null || authorizationLinks.isUsed()) {
            return false;
        }
        logger.info("Auth link has user's id:" + authorizationLinks.getUserId());
        Optional<User> userOpt = userRepository.getById(authorizationLinks.getUserId());

        if (!userOpt.isPresent()) {
            return false;
        }
        User user = userOpt.get();
        authorizationLinks.setUsed(true);
        user.setEnabled(true);
        userRepository.update(user);
        authorizationRepositoryImpl.updateAuthorizationLinks(authorizationLinks);
        return true;
    }

    private User createUser(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isPresent()) {
            throw new FailedToRegisterException("Email is already used");
        }
        //for hashing
         user.setPassword(passwordEncoder.encode(user.getPassword()));

        final User registered = userRepository.insert(user)
                .orElseThrow(() -> new FailedToRegisterException("Error in creating user! Email is free, but creation query failure."));

        logger.info("user is created with id: " + registered.getUserId());

        return registered;
    }

    @Override
    public Page<User> searchUser(String searchExpression, Optional<User> currentUser, int page, int pageSize) {
        searchExpression = "%" + searchExpression.trim() + "%";
        Role user = roleRepository.findByName("USER").orElseThrow(NoUserRoleProvided::new);
        if (!currentUser.isPresent() || roleRepository.getAllRoleById(currentUser.get().getUserId()).contains(user)) {
            int total = userRepository.getFindByEmailOrFullNameFilterByRoleCount(searchExpression, user);
            int pagesCount = pageService.getPagesCount(total, pageSize);
            int currentPage = pageService.getRestrictedPage(page, pagesCount);
            int offset = currentPage * pageSize;
            return new Page<>(page, pagesCount, userRepository.findByEmailOrFullNameFilterByRole(searchExpression, user, offset, pageSize));
        } else {
            int total = userRepository.getFindByEmailOrFullNameFilterByRoleWithoutCount(searchExpression, user);
            int pagesCount = pageService.getPagesCount(total, pageSize);
            int currentPage = pageService.getRestrictedPage(page, pagesCount);
            int offset = currentPage * pageSize;
            return new Page<>(page, pagesCount, userRepository.findByEmailOrFullNameFilterByRoleWithout(searchExpression, user, offset, pageSize));
        }
    }

    @Override
    public User findByUserId(int userId) {
        return userRepository.getById(userId).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        final List<Role> roles = roleRepository.getAllRoleById(user.getUserId());
        user.setRoles(roles);
        return user;
    }

    @Override
    public void updateUser(User oldUser, User newUser) {
        logger.info(newUser + " to update");
        userRepository.update(newUser);
    }

    @Override
    public void updateAdminModerator(User newUser) {
        Optional<User> userFromDB = userRepository.findByEmail(newUser.getEmail());
        List<Role> roles = roleRepository.getAllRoleById(userFromDB.get().getUserId());
        if (roles.size() == 1) {
            for (Role role: roles) {
                if(role.getName().equals("SUPER_ADMIN")){
                    throw new UpdateException("Error in updating");
                }
            }
        }
        if(!userFromDB.get().getEnabled()){
            throw new UpdateException("Error in updating");
        }

        userFromDB.get().setFullName(newUser.getFullName());
        userFromDB.get().setPassword(newUser.getPassword());
        userFromDB.get().setRoles(newUser.getRoles());
        userRepository.update(userFromDB.get());
        userRoleRepositoryImpl.delete(userFromDB.get().getUserId());
        List<Role> setRoles = new ArrayList<>();
        for (Role role : newUser.getRoles()) {
            Optional<Role> roleFromDB = roleRepository.findByName(role.getName());
            setRoles.add(roleFromDB.get());
        }
        for (Role role : setRoles) {
            userRoleRepositoryImpl.insert(userFromDB.get(), role);
        }
    }

    @Override
    public void deleteAdminModerator(String email) {
        final User user = userRepository.findByEmail(email).get();
        List<Role> roles = roleRepository.getAllRoleById(user.getUserId());
        if (roles.size() == 1) {
            for (Role role: roles) {
                if(role.getName().equals("SUPER_ADMIN")){
                    throw new UpdateException("Error in deleting");
                }
            }
        }
        if(user.getEnabled()){
            user.setEnabled(false);
            userRepository.update(user);
        } else {
            throw new UpdateException("Error in deleting");
        }
    }

    public boolean equalsPassword(User user, String rawPassword) {
        logger.info("Old password: " + user.getPassword() + " new password: " + rawPassword);
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

    @Override
    @NonNull
    public List<Integer> getListId() {
        List<Integer> listId = userRepository.getListId();
        return listId != null ? listId : Collections.emptyList();
    }
}
