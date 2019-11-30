package com.netcraker.controllers;

import com.netcraker.model.User;
import com.netcraker.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public abstract class BaseController {
    private final UserService userService;

    public BaseController(UserService userService) {
        this.userService = userService;
    }

    protected Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return Optional.ofNullable(userService.findByEmail(email));
    }
}
