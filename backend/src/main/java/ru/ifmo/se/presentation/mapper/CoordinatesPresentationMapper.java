package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.command.CoordinatesCommand;
import ru.ifmo.se.application.dto.result.CoordinatesResult;
import ru.ifmo.se.presentation.dto.request.CoordinatesRequest;
import ru.ifmo.se.presentation.dto.response.CoordinatesResponse;

@ApplicationScoped
public class CoordinatesPresentationMapper {

    public CoordinatesCommand toCommand(CoordinatesRequest request) {
        if (request == null) {
            return null;
        }
        return new CoordinatesCommand(request.getX(), request.getY());
    }

    public CoordinatesResponse toResponse(CoordinatesResult result) {
        if (result == null) {
            return null;
        }
        return new CoordinatesResponse(result.getX(), result.getY());
    }
}
