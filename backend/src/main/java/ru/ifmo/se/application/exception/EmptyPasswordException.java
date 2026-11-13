package ru.ifmo.se.application.exception;

import java.io.Serial;

public class EmptyPasswordException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EmptyPasswordException() {
        super("Password cannot be empty");
    }

    @Override
    public String getLocalizedMessage() {
        return "Пароль не может быть пустым";
    }
}
