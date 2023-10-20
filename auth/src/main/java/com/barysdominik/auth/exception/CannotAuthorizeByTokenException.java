package com.barysdominik.auth.exception;

public class CannotAuthorizeByTokenException extends RuntimeException{
    public CannotAuthorizeByTokenException() {
        super();
    }

    public CannotAuthorizeByTokenException(String message) {
        super(message);
    }

    public CannotAuthorizeByTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotAuthorizeByTokenException(Throwable cause) {
        super(cause);
    }
}
