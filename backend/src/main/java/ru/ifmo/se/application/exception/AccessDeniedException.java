package ru.ifmo.se.application.exception;

import java.io.Serial;

public class AccessDeniedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AccessDeniedException(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage() != null ? getMessage() : "Доступ запрещен: недостаточно прав";
    }
}
