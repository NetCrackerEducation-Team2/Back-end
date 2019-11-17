package com.netcraker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.User;
import com.netcraker.services.RecoveryService;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationController {

    private final UserService userService;
    private final RecoveryService recoveryService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated User user, BindingResult bindingResult)
            throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User must have only valid properties");
        }

        ObjectMapper mapper = new ObjectMapper();
        String userInJson = mapper.writeValueAsString(user);

        System.out.println("Attempt to register");
        System.out.println(userInJson);


        userService.createUsualUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity activate(@PathVariable String code) {
        boolean isActivated;
        try {
            isActivated = userService.activateUser(code);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new FailedToRegisterException("Invalid activation code. Try to sign up again");
        }

        if (isActivated) {
            System.out.println("Activation code is admitted");
            return new ResponseEntity(HttpStatus.OK);
        }

        System.out.println("Activation code is rejected");

        throw new FailedToRegisterException("Invalid activation code. Try to sign up again");
    }

    @GetMapping("/recovery-link/{email}")
    public ResponseEntity<?> getRecoveryLink(@PathVariable String email) {
        boolean sent = recoveryService.sendRecoveryCode(email);
        if (sent)
            return ResponseEntity.status(HttpStatus.OK).body("Recovery link sent to your email");
        throw new FindException("Recovery link was not sent. Try again");
    }

    @GetMapping("/recover/{code}")
    public ResponseEntity<?> getRecoveryPassword(@PathVariable String code) {
        User user;
        try {
            user = recoveryService.recoverPassword(code)
                    .orElseThrow(() -> new UpdateException("Try to recover password again."));
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to recover password. Try to recover password again.");
        }
        return ResponseEntity.ok(user);
    }
}