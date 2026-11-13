package ru.ifmo.se.application.exception;

import java.io.Serial;

public class ExpiredRefreshTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ExpiredRefreshTokenException() {
        super("Refresh token has expired or was revoked");
    }

    @Override
    public String getLocalizedMessage() {
        return "Refresh токен истек или был отозван";
    }
}
