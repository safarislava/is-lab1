package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.command.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.command.RingCreateCommand;
import ru.ifmo.se.application.dto.result.ObjectsResult;
import ru.ifmo.se.presentation.dto.request.BatchObjectsCreateRequest;
import ru.ifmo.se.presentation.dto.response.BatchObjectsResponse;
import ru.ifmo.se.presentation.dto.response.BookCreatureResponse;
import ru.ifmo.se.presentation.dto.response.MagicCityResponse;
import ru.ifmo.se.presentation.dto.response.RingResponse;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ObjectsPresentationMapper {

    @Inject
    private MagicCityPresentationMapper magicCityMapper;

    @Inject
    private RingPresentationMapper ringMapper;

    @Inject
    private BookCreaturePresentationMapper bookCreatureMapper;

    public ObjectsCreateCommand toCommand(BatchObjectsCreateRequest request) {
        if (request == null) {
            return null;
        }

        List<BookCreatureCreateCommand> creatures = request.getCreatures() == null
                ? Collections.emptyList()
                : request.getCreatures().stream()
                        .map(bookCreatureMapper::toCommand)
                        .toList();

        List<RingCreateCommand> rings = request.getRings() == null
                ? Collections.emptyList()
                : request.getRings().stream()
                        .map(ringMapper::toCommand)
                        .toList();

        List<MagicCityCreateCommand> cities = request.getCities() == null
                ? Collections.emptyList()
                : request.getCities().stream()
                        .map(magicCityMapper::toCommand)
                        .toList();

        return new ObjectsCreateCommand(creatures, rings, cities);
    }

    public BatchObjectsResponse toResponse(ObjectsResult result) {
        if (result == null) {
            return null;
        }

        List<MagicCityResponse> cities = result.getCities() == null
                ? Collections.emptyList()
                : result.getCities().stream()
                        .map(magicCityMapper::toResponse)
                        .toList();

        List<RingResponse> rings = result.getRings() == null
                ? Collections.emptyList()
                : result.getRings().stream()
                        .map(ringMapper::toResponse)
                        .toList();

        List<BookCreatureResponse> creatures = result.getCreatures() == null
                ? Collections.emptyList()
                : result.getCreatures().stream()
                        .map(bookCreatureMapper::toResponse)
                        .toList();

        return new BatchObjectsResponse(cities, rings, creatures);
    }
}
