package com.netcraker.services;

import com.netcraker.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User createUser(User user);
    User findByEmail(String email);
    boolean activateUser(String code);
}
