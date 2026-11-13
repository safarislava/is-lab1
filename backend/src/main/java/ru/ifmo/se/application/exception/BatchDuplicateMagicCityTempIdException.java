package ru.ifmo.se.application.exception;

import java.io.Serial;

public class BatchDuplicateMagicCityTempIdException extends BatchReferenceException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer tempId;

    public BatchDuplicateMagicCityTempIdException(Integer tempId) {
        super(String.format("Duplicate tempId for city: %d", tempId));
        this.tempId = tempId;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Дубликат tempId для города: %d", tempId);
    }
}
