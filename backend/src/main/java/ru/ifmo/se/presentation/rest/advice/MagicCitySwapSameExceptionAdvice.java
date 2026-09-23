package ru.ifmo.se.presentation.rest.advice;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.application.exception.MagicCitySwapSameException;
import ru.ifmo.se.presentation.dto.response.ErrorResponse;

@Provider
public class MagicCitySwapSameExceptionAdvice implements ExceptionMapper<MagicCitySwapSameException> {

    @Override
    public Response toResponse(MagicCitySwapSameException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                Response.Status.BAD_REQUEST.getStatusCode()
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
