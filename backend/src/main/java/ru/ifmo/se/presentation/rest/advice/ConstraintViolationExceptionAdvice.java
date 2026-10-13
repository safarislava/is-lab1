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
import java.util.Map;

@Provider
public class ConstraintViolationExceptionAdvice implements ExceptionMapper<ConstraintViolationException> {

    private static final List<Map.Entry<String, String>> FIELD_TRANSLATIONS = List.of(
            Map.entry("coordinates.x", "Координата X"),
            Map.entry(".x", "Координата X"),
            Map.entry("coordinates.y", "Координата Y"),
            Map.entry(".y", "Координата Y"),
            Map.entry("coordinates", "Координаты"),
            Map.entry("name", "Имя"),
            Map.entry("age", "Возраст"),
            Map.entry("creatureType", "Тип существа"),
            Map.entry("creatureLocationId", "Город"),
            Map.entry("attackLevel", "Уровень атаки"),
            Map.entry("defenseLevel", "Уровень защиты"),
            Map.entry("defense_level", "Уровень защиты"),
            Map.entry("max_attack_level", "Уровень атаки"),
            Map.entry("ringId", "Кольцо"),
            Map.entry("area", "Площадь"),
            Map.entry("populationDensity", "Плотность населения"),
            Map.entry("population", "Население"),
            Map.entry("power", "Сила кольца")
    );

    private static final List<Map.Entry<String, String>> MESSAGE_TRANSLATIONS = List.of(
            Map.entry("must not be blank", "не может быть пустым"),
            Map.entry("must not be null", "обязательно для заполнения"),
            Map.entry("must be greater than 0", "должно быть больше 0"),
            Map.entry("must be less than or equal to 500", "должно быть не больше 500"),
            Map.entry("must be greater than or equal to -780", "должно быть больше -780"),
            Map.entry("must be greater than or equal to 0", "должно быть неотрицательным"),
            Map.entry("must be greater than or equal to 1", "должно быть не меньше 1"),
            Map.entry("must be less than or equal to 100", "должно быть не больше 100")
    );

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> details = exception.getConstraintViolations().stream()
                .map(this::formatViolation)
                .sorted()
                .toList();
        ErrorResponse errorResponse = new ErrorResponse(
                "Ошибка валидации данных",
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
        String path = violation.getPropertyPath().toString();
        String field = translateField(path);
        String message = translateMessage(violation.getMessage());
        return String.format("%s: %s", field, message);
    }

    private String translateField(String path) {
        if (path == null) {
            return "";
        }
        for (Map.Entry<String, String> entry : FIELD_TRANSLATIONS) {
            if (path.endsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return path;
    }

    private String translateMessage(String message) {
        if (message == null) {
            return "некорректное значение";
        }
        for (Map.Entry<String, String> entry : MESSAGE_TRANSLATIONS) {
            if (message.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return message;
    }
}
