package com.netcraker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.model.User;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Validated User user, BindingResult bindingResult)
            throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        String userInJson = mapper.writeValueAsString(user);

        System.out.println("Attempt to register");
        System.out.println(userInJson);

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User must have only valid properties");
        }

        userService.createUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/auth/activate/{code}")
    public ResponseEntity activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            System.out.println("Activation code is admitted");
            return new ResponseEntity(HttpStatus.OK);
        }

        System.out.println("Activation code is rejected");

        throw new FailedToRegisterException("Invalid activation code. Try to sign up again");
    }
}