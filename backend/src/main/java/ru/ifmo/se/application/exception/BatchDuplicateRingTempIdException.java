package ru.ifmo.se.application.exception;

import java.io.Serial;

public class BatchDuplicateRingTempIdException extends BatchReferenceException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer tempId;

    public BatchDuplicateRingTempIdException(Integer tempId) {
        super(String.format("Duplicate tempId for ring: %d", tempId));
        this.tempId = tempId;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Дубликат tempId для кольца: %d", tempId);
    }
}
