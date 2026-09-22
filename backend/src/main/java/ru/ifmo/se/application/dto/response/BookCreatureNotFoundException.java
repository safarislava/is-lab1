package ru.ifmo.se.application.dto.response;

public class BookCreatureNotFoundException extends RuntimeException {
    public BookCreatureNotFoundException(int creatureId) {
        super(String.format("Creature not found: %d", creatureId));
    }
}
