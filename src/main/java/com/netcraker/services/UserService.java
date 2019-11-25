package com.netcraker.services;

import com.netcraker.model.Role;
import com.netcraker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUsualUser(User user);
    User createAdminModerator(User user, List<Role> role);
    User findByUserId(int userId);
    User findByEmail(String email);
    void updateUser(User oldUser, User newUser);
    void updateAdminModerator(User user, List<Role> roles);
    void deleteAdminModerator(int id);
    boolean activateUser(String code);
    boolean equalsPassword(User user, String password);
    User changePassword(int userId, String oldPass, String newPass);
    List<User> searchUser(String searchExpression, Optional<User> currentUser);
}