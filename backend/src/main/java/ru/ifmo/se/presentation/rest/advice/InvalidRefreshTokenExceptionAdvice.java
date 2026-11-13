package ru.ifmo.se.presentation.rest.advice;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.application.exception.InvalidRefreshTokenException;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

@Provider
public class InvalidRefreshTokenExceptionAdvice implements ExceptionMapper<InvalidRefreshTokenException> {

    @Override
    public Response toResponse(InvalidRefreshTokenException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getLocalizedMessage(),
                Response.Status.UNAUTHORIZED.getStatusCode()
        );
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
