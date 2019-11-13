package com.netcraker.controllers;

import com.netcraker.exceptions.FailedToLoginException;
import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.exceptions.FailedToUpdateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler(FailedToRegisterException.class)
    public ResponseEntity<?> handleFailedToRegisterException(FailedToRegisterException e) {

        System.out.println("ErrorHandlerController is handling FailedToRegisterException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(FailedToLoginException.class)
    public ResponseEntity<?> handleFailedToLoginException(FailedToLoginException e){
        System.out.println("FailedToLoginException is handling FailedToRegisterException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(FailedToUpdateUserException.class)
    public ResponseEntity<?> handleFailedToUpdateUserException(FailedToUpdateUserException e) {
        System.out.println("FailedToUpdateUserException is handling FailedToUpdateUserException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
