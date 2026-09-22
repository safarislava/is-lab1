package ru.ifmo.se.application.dto.response;

public class MagicCityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MagicCityNotFoundException(Integer cityId) {
        super(String.format("City not found: %d", cityId));
    }
}
