package com.netcraker.services.impl;

import com.netcraker.exceptions.*;
import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.Page;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.repositories.UserRoleRepository;
import com.netcraker.repositories.impl.AuthorizationRepositoryImpl;
import com.netcraker.services.AuthEmailSenderService;
import com.netcraker.services.PageService;
import com.netcraker.services.UserInfoService;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@PropertySource("classpath:email-messages.properties")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PageService pageService;
    private final UserRepository userRepository;
    private final AuthorizationRepositoryImpl authorizationRepositoryImpl;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthEmailSenderService emailSender;
    private final UserInfoService userInfoService;

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

    @Override
    public User createAdminModerator(User user, List<Role> roles) {
        user.setEnabled(true);
        final User registered = createUser(user);
        for (Role role : roles) {
            Optional<Role> roleFromDB = roleRepository.findByName(role.getName());
            if (!roleFromDB.isPresent()) {
                throw new FindException("Role not found");
            }
            Role roleFind = roleFromDB.get();
            userRoleRepository.insert(registered, roleFind)
                    .orElseThrow(() -> new FailedToRegisterException("Error in creating relationship between user and role"));

        }

        return registered;
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
    public Page<User> searchUser(String searchExpression, int page, int pageSize) {
        User currentUser = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        searchExpression = "%" + searchExpression.trim() + "%";
        Role user = roleRepository.findByName("USER").orElseThrow(NoUserRoleProvided::new);
        if (roleRepository.getAllRoleById(currentUser.getUserId()).contains(user)) {
            return searchFromCasualUsers(searchExpression, currentUser, page, pageSize);
        } else {
            return searchFromAdmins(searchExpression, currentUser, page, pageSize);
        }
    }

    private Page<User> searchFromCasualUsers(String searchExpression, User currentUser, int page, int pageSize) {
        final Role USER = roleRepository.findByName("USER").orElseThrow(NoUserRoleProvided::new);
        int total = userRepository.getFindByEmailOrFullNameFilterByRoleCount(searchExpression, USER, currentUser.getUserId());
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        return new Page<>(page, pagesCount, userRepository.findByEmailOrFullNameFilterByRole(searchExpression, USER, currentUser.getUserId(), offset, pageSize));
    }

    private Page<User> searchFromAdmins(String searchExpression, User currentUser, int page, int pageSize) {
        final Role USER = roleRepository.findByName("USER").orElseThrow(NoUserRoleProvided::new);
        int total = userRepository.getFindByEmailOrFullNameFilterByRoleWithoutCount(searchExpression, USER, currentUser.getUserId());
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        return new Page<>(page, pagesCount, userRepository.findByEmailOrFullNameFilterByRoleWithout(searchExpression, USER, currentUser.getUserId(), offset, pageSize));
    }

    @Override
    public User findByUserId(int userId) {
        User user = userRepository.getById(userId).orElse(null);
        if (user != null) {
            user.setRoles(userRoleRepository.getUserRoles(userId));
        }
        return user;
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
    public void updateAdminModerator(User newUser, List<Role> roles) {
        newUser.setEnabled(true);
        userRepository.update(newUser);
        for (Role role : roles) {
            Optional<Role> roleFromDB = roleRepository.findByName(role.getName());
            if (!roleFromDB.isPresent()) {
                throw new FindException("Role not found");
            }
            Role roleFind = roleFromDB.get();
            userRoleRepository.update(newUser, roleFind);
        }
    }

    @Override
    public void deleteAdminModerator(String email) {
        userRepository.deleteByEmail(email);
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
