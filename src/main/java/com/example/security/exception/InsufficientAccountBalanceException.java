package com.example.security.exception;

import java.io.Serial;

public class InsufficientAccountBalanceException  extends Exception {

    @Serial
    private static final long serialVersionUID = -5302820923472026061L;

    public InsufficientAccountBalanceException() {
        super();
    }

    public InsufficientAccountBalanceException(final String message) {
        super(message);
    }
}
