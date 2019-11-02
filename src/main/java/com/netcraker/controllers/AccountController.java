package com.netcraker.controllers;

import com.netcraker.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@CrossOrigin("localhost:4200")
public class AccountController {

    // for test purpose
    @PostMapping(value = "auth/login" )
    public ResponseEntity<?> testHello() {

        return ResponseEntity.accepted().body("Hello, from POSt auth/login");
    }
}
