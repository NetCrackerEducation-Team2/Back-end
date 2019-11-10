package com.netcraker.exceptions;

public class FailedToUpdateUserException extends RuntimeException {
    public FailedToUpdateUserException(String message) { super(message); }
}