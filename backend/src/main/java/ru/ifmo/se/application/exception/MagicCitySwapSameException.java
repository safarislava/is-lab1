package ru.ifmo.se.application.exception;

import java.io.Serial;

public class MagicCitySwapSameException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer cityId;

    public MagicCitySwapSameException(Integer cityId) {
        super(String.format("Replacement city cannot be the same as the deleted city: %d", cityId));
        this.cityId = cityId;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Город для замены не может совпадать с удаляемым городом (ID %d)", cityId);
    }
}
