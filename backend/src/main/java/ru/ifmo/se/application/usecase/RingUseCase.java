package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.command.RingCreateCommand;
import ru.ifmo.se.application.dto.command.RingUpdateCommand;
import ru.ifmo.se.application.dto.result.RingResult;

import java.util.List;

public interface RingUseCase {
    RingResult create(RingCreateCommand command);

    RingResult getById(int id);

    RingResult update(int id, RingUpdateCommand command);

    void delete(int id);

    List<RingResult> getAll();
}
