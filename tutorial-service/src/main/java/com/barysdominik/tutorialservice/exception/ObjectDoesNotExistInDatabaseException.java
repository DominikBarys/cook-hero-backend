package com.barysdominik.tutorialservice.exception;

public class ObjectDoesNotExistInDatabaseException extends RuntimeException{
    public ObjectDoesNotExistInDatabaseException() {
        super();
    }

    public ObjectDoesNotExistInDatabaseException(String message) {
        super(message);
    }

    public ObjectDoesNotExistInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectDoesNotExistInDatabaseException(Throwable cause) {
        super(cause);
    }
}
