package com.netcraker.exceptions;

public class RequiresAuthenticationException extends RuntimeException {
    public RequiresAuthenticationException() {
        super("Access to this resource requires authentication");
    }
}
