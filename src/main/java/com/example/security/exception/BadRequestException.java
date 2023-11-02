package com.example.security.exception;

import java.io.Serial;

public class BadRequestException extends Exception {

    @Serial
    private static final long serialVersionUID = 523016048628748232L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(final String message) {
        super(message);
    }
}
