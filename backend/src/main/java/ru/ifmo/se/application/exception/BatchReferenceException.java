package ru.ifmo.se.application.exception;

import java.io.Serial;

public class BatchReferenceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BatchReferenceException(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
}
