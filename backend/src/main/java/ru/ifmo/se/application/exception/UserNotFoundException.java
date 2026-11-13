package ru.ifmo.se.application.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String identifier;
    private final boolean isId;

    public UserNotFoundException(String username) {
        super(String.format("User not found: %s", username));
        this.identifier = username;
        this.isId = false;
    }

    public UserNotFoundException(int id) {
        super(String.format("User not found with id: %d", id));
        this.identifier = String.valueOf(id);
        this.isId = true;
    }

    @Override
    public String getLocalizedMessage() {
        if (isId) {
            return String.format("Пользователь с ID %s не найден", identifier);
        }
        return String.format("Пользователь '%s' не найден", identifier);
    }
}
