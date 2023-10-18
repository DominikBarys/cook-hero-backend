package com.barysdominik.auth.exception;

public class UserDontExistException extends RuntimeException{
    public UserDontExistException() {
        super();
    }

    public UserDontExistException(String message) {
        super(message);
    }

    public UserDontExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDontExistException(Throwable cause) {
        super(cause);
    }
}
