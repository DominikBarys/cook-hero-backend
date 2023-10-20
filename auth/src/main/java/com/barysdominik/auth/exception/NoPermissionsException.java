package com.barysdominik.auth.exception;

public class NoPermissionsException extends RuntimeException{
    public NoPermissionsException() {
        super();
    }

    public NoPermissionsException(String message) {
        super(message);
    }

    public NoPermissionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionsException(Throwable cause) {
        super(cause);
    }
}
