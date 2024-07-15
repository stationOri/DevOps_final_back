package org.example.oristationbackend.exception;

public class DuplicateLikeException extends RuntimeException {
    public DuplicateLikeException() {
    }

    public DuplicateLikeException(String message) {
        super(message);
    }

    public DuplicateLikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateLikeException(Throwable cause) {
        super(cause);
    }

    public DuplicateLikeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
