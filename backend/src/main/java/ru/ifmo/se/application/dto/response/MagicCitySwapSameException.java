package ru.ifmo.se.application.dto.response;

public class MagicCitySwapSameException extends RuntimeException {
    public MagicCitySwapSameException(Integer cityId) {
        super(String.format("Replacement city cannot be the same as the deleted city:  %d", cityId));
    }
}
