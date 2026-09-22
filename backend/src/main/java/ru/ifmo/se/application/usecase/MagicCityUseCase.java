package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.request.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.request.MagicCityUpdateCommand;
import ru.ifmo.se.application.dto.response.MagicCityResponse;

import java.util.List;

public interface MagicCityUseCase {
    MagicCityResponse create(MagicCityCreateCommand command);

    MagicCityResponse getById(int id);

    MagicCityResponse update(int id, MagicCityUpdateCommand command);

    void delete(int id);

    List<MagicCityResponse> getAll();
}
