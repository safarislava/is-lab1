package ru.ifmo.se.application.exception;

import java.io.Serial;

public class InvalidCredentialsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException() {
        super("Invalid username or password");
    }

    @Override
    public String getLocalizedMessage() {
        return "Неверное имя пользователя или пароль";
    }
}
