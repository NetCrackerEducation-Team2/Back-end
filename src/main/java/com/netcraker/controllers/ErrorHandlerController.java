package com.netcraker.controllers;

import com.netcraker.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.NoSuchAlgorithmException;

@ControllerAdvice
public class ErrorHandlerController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);
    @ExceptionHandler(FailedToRegisterException.class)
    public ResponseEntity<?> handleFailedToRegisterException(FailedToRegisterException e) {
        logger.error("ErrorHandlerController is handling FailedToRegisterException");

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(FailedToLoginException.class)
    public ResponseEntity<?> handleFailedToLoginException(FailedToLoginException e){
        logger.error("ErrorHandlerController is handling FailedToRegisterException");

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(CreationException.class)
    public ResponseEntity<?> handleCreationException(CreationException e) {
        logger.error("ErrorHandlerController is handling CreationException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<?> handleUpdateException(UpdateException e) {
        logger.error("ErrorHandlerController is handling CreationException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    @ExceptionHandler(FindException.class)
    public ResponseEntity<?> handleFindException(FindException e) {
        logger.error("ErrorHandlerController is handling FindException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<?> handleNoSuchAlgorithmException(NoSuchAlgorithmException e) {
        logger.error("ErrorHandlerController is handling NoSuchAlgorithmException");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("ErrorHandlerController is handling IllegalArgumentException");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
