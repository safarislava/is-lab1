package ru.ifmo.se.presentation.rest.advice;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Provider
public class WebApplicationExceptionAdvice implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            Response.Status statusEnum = Response.Status.fromStatusCode(status);
            message = statusEnum != null ? statusEnum.getReasonPhrase() : "HTTP Error " + status;
        }
        ErrorResponse errorResponse = new ErrorResponse(
                message,
                status,
                ZonedDateTime.now(),
                List.of()
        );
        return Response.status(status)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
