package ru.ifmo.se.presentation.rest.advice;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.application.exception.EmptyPasswordException;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

@Provider
public class EmptyPasswordExceptionAdvice implements ExceptionMapper<EmptyPasswordException> {

    @Override
    public Response toResponse(EmptyPasswordException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getLocalizedMessage(),
                Response.Status.BAD_REQUEST.getStatusCode()
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
