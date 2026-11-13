package ru.ifmo.se.application.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String username;

    public UserNotFoundException(String username) {
        super(String.format("User not found: %s", username));
        this.username = username;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Пользователь '%s' не найден", username);
    }
}
