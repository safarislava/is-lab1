package ru.ifmo.se.application.exception;

import java.io.Serial;

public class BookCreatureNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int creatureId;

    public BookCreatureNotFoundException(int creatureId) {
        super(String.format("Creature not found: %d", creatureId));
        this.creatureId = creatureId;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("Существо с ID %d не найдено", creatureId);
    }
}
