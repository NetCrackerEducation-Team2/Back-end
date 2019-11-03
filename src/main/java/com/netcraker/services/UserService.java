package com.netcraker.services;

import com.netcraker.model.User;

public interface UserService {
    User saveUser(User user);
    User getUser(String username, String password);
    void deleteUser(Long id);

}