package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.command.RingCreateCommand;
import ru.ifmo.se.application.dto.command.RingUpdateCommand;
import ru.ifmo.se.application.dto.result.RingResult;
import ru.ifmo.se.persistence.entity.Ring;

@ApplicationScoped
public class RingMapper {

    public void updateEntity(Ring ring, RingUpdateCommand command) {
        if (ring == null || command == null) {
            return;
        }
        ring.setName(command.getName());
        ring.setPower(command.getPower());
    }

    public Ring toEntity(RingCreateCommand command) {
        if (command == null) {
            return null;
        }
        Ring ring = new Ring();
        ring.setName(command.getName());
        ring.setPower(command.getPower());
        return ring;
    }

    public RingResult toResult(Ring ring) {
        if (ring == null) {
            return null;
        }
        return new RingResult(ring.getId(), ring.getName(), ring.getPower());
    }
}
