package com.netcraker.controllers;

import com.netcraker.exceptions.FailedToRegisterException;
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
}
