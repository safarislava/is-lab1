package ru.ifmo.se.application.exception;

import java.io.Serial;

public class UserAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String username;

    public UserAlreadyExistsException(String username) {
        super(String.format("User already exists: %s", username));
        this.username = username;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Пользователь с именем '%s' уже существует", username);
    }
}
