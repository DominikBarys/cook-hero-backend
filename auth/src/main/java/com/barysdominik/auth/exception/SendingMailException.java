package com.barysdominik.auth.exception;

public class SendingMailException extends RuntimeException{
    public SendingMailException() {
        super();
    }

    public SendingMailException(String message) {
        super(message);
    }

    public SendingMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendingMailException(Throwable cause) {
        super(cause);
    }
}
