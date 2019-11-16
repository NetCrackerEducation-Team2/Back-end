package com.netcraker.controllers;

import com.netcraker.exceptions.*;
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
        System.out.println("ErrorHandlerController is handling FailedToRegisterException");

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(CreationException.class)
    public ResponseEntity<?> handleCreationException(CreationException e) {
        System.out.println("ErrorHandlerController is handling CreationException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<?> handleUpdateException(UpdateException e) {
        System.out.println("ErrorHandlerController is handling CreationException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    @ExceptionHandler(FindException.class)
    public ResponseEntity<?> handleFindException(FindException e) {
        System.out.println("ErrorHandlerController is handling FindException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
