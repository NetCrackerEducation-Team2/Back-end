package com.netcraker.services;

import com.netcraker.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity createUser(User user);
    boolean activateUser(String code);
    User getUser(int userId);
}