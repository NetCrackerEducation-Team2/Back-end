package com.netcraker.services.impl;

import com.netcraker.exceptions.*;
import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.Page;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.repositories.AuthorizationRepository;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.repositories.UserRoleRepository;
import com.netcraker.services.AuthEmailSenderService;
import com.netcraker.services.PageService;
import com.netcraker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
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
    private final AuthorizationRepository authorizationRepositoryImpl;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
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
            roleFromDB.ifPresent(setRoles::add);
        }
        user.setRoles(setRoles);
        return createUser(user);
    }

    @Override
    public boolean activateUser(String token) {
        Optional<AuthorizationLinks> authorizationLinks = authorizationRepositoryImpl.findByActivationCode(token);
        if(!authorizationLinks.isPresent() || authorizationLinks.get().isUsed()) {
            return false;
        }
        logger.info("Auth link has user's id:" + authorizationLinks.get().getUserId());
        Optional<User> userOpt = userRepository.getById(authorizationLinks.get().getUserId());

        if (!userOpt.isPresent()) {
            return false;
        }
        User user = userOpt.get();
        authorizationLinks.get().setUsed(true);
        user.setEnabled(true);
        userRepository.update(user);
        authorizationRepositoryImpl.updateAuthorizationLinks(authorizationLinks.get());
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
        List<Role> roles = new ArrayList<>();
        if(userFromDB.isPresent()){
            roles = roleRepository.getAllRoleById(userFromDB.get().getUserId());
        }
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
        userFromDB.get().setPassword(passwordEncoder.encode(newUser.getPassword()));
        userFromDB.get().setRoles(newUser.getRoles());
        userRepository.update(userFromDB.get());
        userRoleRepository.delete(userFromDB.get().getUserId());
        List<Role> setRoles = new ArrayList<>();
        for (Role role : newUser.getRoles()) {
            Optional<Role> roleFromDB = roleRepository.findByName(role.getName());
            roleFromDB.ifPresent(setRoles::add);
        }
        for (Role role : setRoles) {
            userRoleRepository.insert(userFromDB.get(), role);
        }
    }

    @Override
    public void deleteAdminModerator(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()){
            throw new DeleteException("Error in deleting");
        }
        List<Role> roles = roleRepository.getAllRoleById(user.get().getUserId());
        if (roles.size() == 1) {
            for (Role role: roles) {
                if(role.getName().equals("SUPER_ADMIN")){
                    throw new UpdateException("Error in deleting");
                }
            }
        }
        if(user.get().getEnabled()){
            user.get().setEnabled(false);
            userRepository.update(user.get());
        } else {
            throw new DeleteException("Error in deleting");
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
