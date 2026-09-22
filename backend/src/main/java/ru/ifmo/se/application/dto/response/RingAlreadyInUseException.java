package ru.ifmo.se.application.dto.response;

import ru.ifmo.se.persistence.entity.BookCreature;

public class RingAlreadyInUseException extends RuntimeException {
    public RingAlreadyInUseException(Integer creatureId) {
        super(String.format("Ring is already in use by creature: %d", creatureId));
    }
}
