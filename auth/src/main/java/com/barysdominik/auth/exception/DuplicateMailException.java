package com.barysdominik.auth.exception;

public class DuplicateMailException extends RuntimeException{
    public DuplicateMailException() {
        super();
    }

    public DuplicateMailException(String message) {
        super(message);
    }

    public DuplicateMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateMailException(Throwable cause) {
        super(cause);
    }
}
