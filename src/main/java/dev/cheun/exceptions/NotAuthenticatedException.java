package dev.cheun.exceptions;

// To correspond with HTTP 401 status.
public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
