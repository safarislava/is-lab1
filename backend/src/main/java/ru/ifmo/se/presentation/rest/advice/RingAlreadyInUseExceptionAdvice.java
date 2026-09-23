package ru.ifmo.se.presentation.rest.advice;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.application.exception.RingAlreadyInUseException;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

@Provider
public class RingAlreadyInUseExceptionAdvice implements ExceptionMapper<RingAlreadyInUseException> {

    @Override
    public Response toResponse(RingAlreadyInUseException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                Response.Status.CONFLICT.getStatusCode()
        );
        return Response.status(Response.Status.CONFLICT)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
