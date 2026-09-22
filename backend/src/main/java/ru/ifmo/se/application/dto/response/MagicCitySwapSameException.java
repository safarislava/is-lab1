package ru.ifmo.se.application.dto.response;

import java.io.Serial;

public class MagicCitySwapSameException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MagicCitySwapSameException(Integer cityId) {
        super(String.format("Replacement city cannot be the same as the deleted city: %d", cityId));
    }
}
