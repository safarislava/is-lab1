package ru.ifmo.se.application.exception;

import ru.ifmo.se.persistence.entity.BookCreature;

import java.io.Serial;

public class RingAlreadyInUseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RingAlreadyInUseException(BookCreature bookCreature) {
        super(String.format("Ring is already in use by creature: %s", bookCreature.getName()));
    }
}
