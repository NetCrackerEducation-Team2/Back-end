package com.netcraker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.listeners.WebSocketEventListener;
import com.netcraker.model.User;
import com.netcraker.services.RecoveryService;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequiredArgsConstructor
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
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

        logger.info("Attempt to register");
        logger.info(userInJson);


        userService.createUsualUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            logger.info("Activation code is admitted");
            return new ResponseEntity(HttpStatus.OK);
        }

        logger.info("Activation code is rejected");
        throw new UpdateException("Invalid activation code. Try to sign up again");
    }

    @GetMapping("/recovery-link/{email}")
    public ResponseEntity<?> getRecoveryLink(@PathVariable String email) {
        long start = System.currentTimeMillis();

        boolean sent = recoveryService.sendRecoveryCode(email);
        if (sent) {
            System.out.println("Time spent to mail recovery link: " + (System.currentTimeMillis() - start));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new FindException("Recovery link was not sent. Try again");
    }

    @GetMapping("/recover/{code}")
    public ResponseEntity<?> getRecoveryPassword(@PathVariable String code) {
        User user = recoveryService.recoverPassword(code)
                .orElseThrow(() -> new UpdateException("Try to recover password again."));
        return ResponseEntity.ok(user);
    }
}