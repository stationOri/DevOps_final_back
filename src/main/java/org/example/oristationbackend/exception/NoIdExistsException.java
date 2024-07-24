package org.example.oristationbackend.exception;

public class NoIdExistsException extends RuntimeException {
    public NoIdExistsException() {
    }

    public NoIdExistsException(String message) {
        super(message);
    }

    public NoIdExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoIdExistsException(Throwable cause) {
        super(cause);
    }

    public NoIdExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
