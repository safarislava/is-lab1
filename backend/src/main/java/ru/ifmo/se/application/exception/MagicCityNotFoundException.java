package ru.ifmo.se.application.exception;

import java.io.Serial;

public class MagicCityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer cityId;

    public MagicCityNotFoundException(Integer cityId) {
        super(String.format("City not found: %d", cityId));
        this.cityId = cityId;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Город с ID %d не найден", cityId);
    }
}
