package ru.ifmo.se.application.dto.response;

import java.io.Serial;

public class RingNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RingNotFoundException(Integer ringId) {
        super(String.format("Ring not found: %d", ringId));
    }
}
