package com.netcraker.controllers;

import com.netcraker.model.User;
import com.netcraker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/profile/{userId}")
    public User getUserProfile(@PathVariable int userId) {
        return userService.getUser(userId);
    }

}
