package ru.ifmo.se.application.exception;

import java.io.Serial;

public class BatchMagicCityNotFoundException extends BatchReferenceException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer tempId;

    public BatchMagicCityNotFoundException(Integer tempId) {
        super(String.format("City with tempId %d not found in batch", tempId));
        this.tempId = tempId;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Город с tempId %d не найден в текущем пакете", tempId);
    }
}
