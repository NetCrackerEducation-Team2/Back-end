package com.netcraker.services;

import com.netcraker.model.Role;
import com.netcraker.model.User;

public interface UserService {
    User createUser(User user);
    User createAdminModerator(User user, Role role);
    void updateUserRole(Role oldRole, User newUser);
//    void deleteAdminModerator(Role role);
    boolean activateUser(String code);
    User findByUserId(int userId);
    User findByEmail(String email);
    void updateUser(User oldUser, User newUser);
}