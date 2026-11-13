package ru.ifmo.se.application.exception;

import java.io.Serial;

public class BatchRingNotFoundException extends BatchReferenceException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer tempId;

    public BatchRingNotFoundException(Integer tempId) {
        super(String.format("Ring with tempId %d not found in batch", tempId));
        this.tempId = tempId;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Кольцо с tempId %d не найдено в текущем пакете", tempId);
    }
}
