package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.command.BatchBookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.BatchMagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.BatchRingCreateCommand;
import ru.ifmo.se.application.dto.command.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.RingCreateCommand;

@ApplicationScoped
public class ObjectsMapper {

    public RingCreateCommand toCommand(BatchRingCreateCommand command) {
        if (command == null) {
            return null;
        }
        return new RingCreateCommand(command.getName(), command.getPower());
    }

    public MagicCityCreateCommand toCommand(BatchMagicCityCreateCommand command) {
        if (command == null) {
            return null;
        }
        return new MagicCityCreateCommand(
                command.getName(),
                command.getArea(),
                command.getPopulation(),
                command.getEstablishmentDate(),
                command.getGovernor(),
                command.getCapital(),
                command.getPopulationDensity()
        );
    }

    public BookCreatureCreateCommand toCommand(BatchBookCreatureCreateCommand command) {
        if (command == null) {
            return null;
        }
        return new BookCreatureCreateCommand(
                command.getName(),
                command.getCoordinates(),
                command.getAge(),
                command.getCreatureType(),
                command.getCreatureLocationId(),
                command.getAttackLevel(),
                command.getDefenseLevel(),
                command.getRingId()
        );
    }
}
