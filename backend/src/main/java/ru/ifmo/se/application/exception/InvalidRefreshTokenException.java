package ru.ifmo.se.application.exception;

import java.io.Serial;

public class InvalidRefreshTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidRefreshTokenException() {
        super("Invalid refresh token");
    }

    @Override
    public String getLocalizedMessage() {
        return "Недействительный refresh токен";
    }
}
