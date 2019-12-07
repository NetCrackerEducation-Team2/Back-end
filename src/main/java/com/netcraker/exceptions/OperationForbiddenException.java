package com.netcraker.exceptions;

public class OperationForbiddenException extends RuntimeException {
    public OperationForbiddenException() {
    }

    public OperationForbiddenException(String message) {
        super(message);
    }

    public OperationForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationForbiddenException(Throwable cause) {
        super(cause);
    }

    public OperationForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
