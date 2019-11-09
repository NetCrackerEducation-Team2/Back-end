package com.netcraker.exceptions;

import org.springframework.http.HttpStatus;

public class FailedToRegisterException extends RuntimeException {
    public FailedToRegisterException(String message) {
        super(message);
    }
    public FailedToRegisterException(String message, HttpStatus httpStatus) {
        super(message);
    }
}
