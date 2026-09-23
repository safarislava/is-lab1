package ru.ifmo.se.presentation.rest.advice;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Provider
public class GlobalExceptionAdvice implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        String message = exception.getMessage() != null && !exception.getMessage().isBlank()
                ? exception.getMessage()
                : "Internal server error";
        ErrorResponse errorResponse = new ErrorResponse(
                message,
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                ZonedDateTime.now(),
                List.of()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
