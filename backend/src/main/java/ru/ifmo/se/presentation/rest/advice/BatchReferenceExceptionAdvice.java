package ru.ifmo.se.presentation.rest.advice;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.application.exception.BatchReferenceException;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Provider
public class BatchReferenceExceptionAdvice implements ExceptionMapper<BatchReferenceException> {

    @Override
    public Response toResponse(BatchReferenceException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getLocalizedMessage(),
                Response.Status.BAD_REQUEST.getStatusCode(),
                ZonedDateTime.now(),
                List.of()
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
