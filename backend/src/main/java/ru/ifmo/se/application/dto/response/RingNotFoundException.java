package ru.ifmo.se.application.dto.response;

public class RingNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RingNotFoundException(Integer ringId) {
        super(String.format("Ring not found: %d", ringId));
    }
}
