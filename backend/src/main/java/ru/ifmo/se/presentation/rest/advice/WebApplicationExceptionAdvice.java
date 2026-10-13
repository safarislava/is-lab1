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
        String message = resolveStatusMessage(status);
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

    private String resolveStatusMessage(int status) {
        return switch (status) {
            case 400 -> "Некорректный запрос";
            case 401 -> "Требуется авторизация";
            case 403 -> "Доступ запрещен";
            case 404 -> "Запрашиваемый ресурс не найден";
            case 405 -> "Метод HTTP не поддерживается";
            case 409 -> "Конфликт данных";
            case 500 -> "Внутренняя ошибка сервера";
            default -> "Ошибка HTTP " + status;
        };
    }
}
