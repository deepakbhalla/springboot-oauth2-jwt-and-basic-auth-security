package com.example.security.exception;

import java.io.Serial;

public class ArgumentValidationException extends Exception {

    @Serial
    private static final long serialVersionUID = -5302820923472026061L;

    public ArgumentValidationException() {
        super();
    }

    public ArgumentValidationException(final String message) {
        super(message);
    }
}