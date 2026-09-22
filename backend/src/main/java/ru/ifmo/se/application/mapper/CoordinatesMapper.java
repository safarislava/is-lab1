package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.command.CoordinatesCommand;
import ru.ifmo.se.application.dto.result.CoordinatesResult;
import ru.ifmo.se.persistence.entity.Coordinates;

@ApplicationScoped
public class CoordinatesMapper {

    public Coordinates toEntity(CoordinatesCommand command) {
        if (command == null) {
            return null;
        }
        return new Coordinates(command.getX(), command.getY());
    }

    public CoordinatesResult toResult(Coordinates coordinates) {
        if (coordinates == null) {
            return null;
        }
        return new CoordinatesResult(coordinates.getX(), coordinates.getY());
    }
}
