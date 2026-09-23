package ru.ifmo.se.application.exception;

import java.io.Serial;

public class MagicCityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MagicCityNotFoundException(Integer cityId) {
        super(String.format("City not found: %d", cityId));
    }
}
