package com.netcraker.exceptions;

public class FailedToLoginException extends RuntimeException{
    public FailedToLoginException(String message) {
        super(message);
    }
}
