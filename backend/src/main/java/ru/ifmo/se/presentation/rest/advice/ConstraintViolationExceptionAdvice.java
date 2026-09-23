package ru.ifmo.se.presentation.rest.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Provider
public class ConstraintViolationExceptionAdvice implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> details = exception.getConstraintViolations().stream()
                .map(this::formatViolation)
                .sorted()
                .toList();
        ErrorResponse errorResponse = new ErrorResponse(
                "Validation failed",
                Response.Status.BAD_REQUEST.getStatusCode(),
                ZonedDateTime.now(),
                details
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private String formatViolation(ConstraintViolation<?> violation) {
        return String.format("%s: %s", violation.getPropertyPath(), violation.getMessage());
    }
}
