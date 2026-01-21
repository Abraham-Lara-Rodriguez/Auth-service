package com.abrahamlara.authservice.shared.dto;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a provided token is invalid or cannot be used.
 */
public class InvalidTokenException extends RuntimeException {

    private final HttpStatus status;

    public InvalidTokenException(String message) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED; // 401 by default
    }

    public InvalidTokenException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
