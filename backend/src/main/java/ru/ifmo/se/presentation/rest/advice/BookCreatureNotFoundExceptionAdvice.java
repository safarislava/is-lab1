package ru.ifmo.se.presentation.rest.advice;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.application.exception.BookCreatureNotFoundException;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

@Provider
public class BookCreatureNotFoundExceptionAdvice implements ExceptionMapper<BookCreatureNotFoundException> {

    @Override
    public Response toResponse(BookCreatureNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                Response.Status.NOT_FOUND.getStatusCode()
        );
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
