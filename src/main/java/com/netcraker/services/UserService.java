package com.netcraker.services;

import com.netcraker.model.User;

public interface UserService {
    User registerUser(User user);
    User findUser(String username, String password);
    void deleteUser(Long id);

}