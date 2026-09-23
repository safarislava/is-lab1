package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.command.RingCreateCommand;
import ru.ifmo.se.application.dto.command.RingUpdateCommand;
import ru.ifmo.se.application.dto.result.RingResult;
import ru.ifmo.se.presentation.dto.request.RingCreateRequest;
import ru.ifmo.se.presentation.dto.request.RingUpdateRequest;
import ru.ifmo.se.presentation.dto.response.RingResponse;

@ApplicationScoped
public class RingPresentationMapper {

    public RingCreateCommand toCommand(RingCreateRequest request) {
        if (request == null) {
            return null;
        }
        return new RingCreateCommand(request.getName(), request.getPower());
    }

    public RingUpdateCommand toCommand(RingUpdateRequest request) {
        if (request == null) {
            return null;
        }
        return new RingUpdateCommand(request.getName(), request.getPower());
    }

    public RingResponse toResponse(RingResult result) {
        if (result == null) {
            return null;
        }
        return new RingResponse(result.getId(), result.getName(), result.getPower());
    }
}
