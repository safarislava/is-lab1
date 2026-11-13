package ru.ifmo.se.application.exception;

import java.io.Serial;

public class UnauthorizedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage() != null ? getMessage() : "Требуется авторизация";
    }
}
