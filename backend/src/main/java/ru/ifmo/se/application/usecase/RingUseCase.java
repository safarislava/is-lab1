package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.request.RingCreateCommand;
import ru.ifmo.se.application.dto.request.RingUpdateCommand;
import ru.ifmo.se.application.dto.response.RingResponse;

import java.util.List;

public interface RingUseCase {
    RingResponse create(RingCreateCommand command);

    RingResponse getById(int id);

    RingResponse update(int id, RingUpdateCommand command);

    void delete(int id);

    List<RingResponse> getAll();
}
