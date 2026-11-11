package ru.ifmo.se.application.exception;

import java.io.Serial;

public class RingNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer ringId;

    public RingNotFoundException(Integer ringId) {
        super(String.format("Ring not found: %d", ringId));
        this.ringId = ringId;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Кольцо с ID %d не найдено", ringId);
    }
}
