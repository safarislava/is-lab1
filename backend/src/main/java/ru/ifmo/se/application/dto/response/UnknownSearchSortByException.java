package ru.ifmo.se.application.dto.response;

import java.io.Serial;

public class UnknownSearchSortByException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnknownSearchSortByException(String parameter) {
        super(String.format("Unknown sort by: %s", parameter));
    }
}
