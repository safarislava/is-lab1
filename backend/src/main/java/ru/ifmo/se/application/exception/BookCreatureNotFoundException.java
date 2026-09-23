package ru.ifmo.se.application.exception;

import java.io.Serial;

public class BookCreatureNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BookCreatureNotFoundException(int creatureId) {
        super(String.format("Creature not found: %d", creatureId));
    }
}
