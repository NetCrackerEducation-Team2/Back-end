package com.netcraker.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    // for test purpose
    @GetMapping("auth/login")
    public String testHello(){
        return "Hello from auth";
    }
}
