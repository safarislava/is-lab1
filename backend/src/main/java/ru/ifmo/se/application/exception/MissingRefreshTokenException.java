package ru.ifmo.se.application.exception;

import java.io.Serial;

public class MissingRefreshTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MissingRefreshTokenException() {
        super("Refresh token is missing");
    }

    @Override
    public String getLocalizedMessage() {
        return "Refresh токен отсутствует";
    }
}
