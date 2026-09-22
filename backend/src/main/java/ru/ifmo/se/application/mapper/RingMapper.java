package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.request.RingCreateCommand;
import ru.ifmo.se.application.dto.response.RingResponse;
import ru.ifmo.se.persistence.entity.Ring;

@ApplicationScoped
public class RingMapper {

    public Ring toEntity(RingCreateCommand command) {
        if (command == null) {
            return null;
        }
        Ring ring = new Ring();
        ring.setName(command.getName());
        ring.setPower(command.getPower());
        return ring;
    }

    public RingResponse toResponse(Ring ring) {
        if (ring == null) {
            return null;
        }
        return new RingResponse(ring.getId(), ring.getName(), ring.getPower());
    }
}
