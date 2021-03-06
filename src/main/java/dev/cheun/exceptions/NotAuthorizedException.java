package dev.cheun.exceptions;

// To correspond with HTTP 403 status.
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
