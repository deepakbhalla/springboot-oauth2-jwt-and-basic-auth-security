package com.example.security.exception;

import java.io.Serial;

public class ResourceNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = -5302820923472026061L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
