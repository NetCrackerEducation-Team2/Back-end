package com.netcraker.controllers;
import com.netcraker.model.User;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationController {

    private final @NonNull UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity create(@RequestBody User user) {
        return userService.createUser(user);
    }
    @GetMapping("/activate/{code}")
    public ResponseEntity activate(@PathVariable String code){
        boolean isActivated = userService.activateUser(code);
        if(isActivated){
           return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}