package com.example.security.exception;

import java.io.Serial;

public class UserAlreadyExistAuthenticationException  extends Exception {

    @Serial
    private static final long serialVersionUID = -5302820923472026061L;

    public UserAlreadyExistAuthenticationException() {
        super();
    }

    public UserAlreadyExistAuthenticationException(final String message) {
        super(message);
    }
}