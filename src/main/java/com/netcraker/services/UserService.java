package com.netcraker.services;

import com.netcraker.model.User;

public interface UserService {
    User createUser(User user);
    boolean activateUser(String code);
    User findByUserId(int userId);
    User findByEmail(String email);
    void updateUser(User oldUser, User newUser);
}