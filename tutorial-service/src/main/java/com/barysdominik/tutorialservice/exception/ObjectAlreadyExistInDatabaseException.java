package com.barysdominik.tutorialservice.exception;

public class ObjectAlreadyExistInDatabaseException extends RuntimeException{
    public ObjectAlreadyExistInDatabaseException() {
        super();
    }

    public ObjectAlreadyExistInDatabaseException(String message) {
        super(message);
    }

    public ObjectAlreadyExistInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectAlreadyExistInDatabaseException(Throwable cause) {
        super(cause);
    }
}
