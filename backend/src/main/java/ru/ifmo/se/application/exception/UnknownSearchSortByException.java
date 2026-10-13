package ru.ifmo.se.application.exception;

import java.io.Serial;

public class UnknownSearchSortByException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String parameter;

    public UnknownSearchSortByException(String parameter) {
        super(String.format("Unknown sort by: %s", parameter));
        this.parameter = parameter;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Неизвестное поле сортировки: %s", parameter);
    }
}
