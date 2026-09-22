package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.request.CoordinatesRequest;
import ru.ifmo.se.application.dto.response.CoordinatesResponse;
import ru.ifmo.se.persistence.entity.Coordinates;

@ApplicationScoped
public class CoordinatesMapper {

    public Coordinates toEntity(CoordinatesRequest request) {
        if (request == null) {
            return null;
        }
        return new Coordinates(request.getX(), request.getY());
    }

    public CoordinatesResponse toResponse(Coordinates coordinates) {
        if (coordinates == null) {
            return null;
        }
        return new CoordinatesResponse(coordinates.getX(), coordinates.getY());
    }
}
