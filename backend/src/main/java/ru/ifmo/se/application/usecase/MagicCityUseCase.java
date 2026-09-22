package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.command.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.MagicCityUpdateCommand;
import ru.ifmo.se.application.dto.result.MagicCityResult;

import java.util.List;

public interface MagicCityUseCase {
    MagicCityResult create(MagicCityCreateCommand command);

    MagicCityResult getById(int id);

    MagicCityResult update(int id, MagicCityUpdateCommand command);

    void delete(int id, int replacementCityId);

    List<MagicCityResult> getAll();
}
