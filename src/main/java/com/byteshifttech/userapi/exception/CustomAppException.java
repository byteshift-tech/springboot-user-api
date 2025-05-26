package com.byteshifttech.userapi.exception;

import lombok.Getter;

/**
 * Custom exception class for domain-specific business errors.
 */
@Getter
public class CustomAppException extends RuntimeException {

    private final int statusCode;

    public CustomAppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
