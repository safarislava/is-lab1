package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.command.BatchBookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.BatchMagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.BatchRingCreateCommand;
import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.result.ObjectsResult;
import ru.ifmo.se.presentation.dto.request.BatchBookCreatureCreateRequest;
import ru.ifmo.se.presentation.dto.request.BatchMagicCityCreateRequest;
import ru.ifmo.se.presentation.dto.request.BatchObjectsCreateRequest;
import ru.ifmo.se.presentation.dto.request.BatchRingCreateRequest;
import ru.ifmo.se.presentation.dto.response.BatchObjectsResponse;
import ru.ifmo.se.presentation.dto.response.BookCreatureResponse;
import ru.ifmo.se.presentation.dto.response.MagicCityResponse;
import ru.ifmo.se.presentation.dto.response.RingResponse;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ObjectsPresentationMapper {

    @Inject
    private CoordinatesPresentationMapper coordinatesMapper;

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

        List<BatchBookCreatureCreateCommand> creatures = request.getCreatures() == null
                ? Collections.emptyList()
                : request.getCreatures().stream()
                        .map(this::toCommand)
                        .toList();

        List<BatchRingCreateCommand> rings = request.getRings() == null
                ? Collections.emptyList()
                : request.getRings().stream()
                        .map(this::toCommand)
                        .toList();

        List<BatchMagicCityCreateCommand> cities = request.getCities() == null
                ? Collections.emptyList()
                : request.getCities().stream()
                        .map(this::toCommand)
                        .toList();

        return new ObjectsCreateCommand(creatures, rings, cities);
    }

    public BatchRingCreateCommand toCommand(BatchRingCreateRequest request) {
        if (request == null) {
            return null;
        }
        return new BatchRingCreateCommand(request.getTempId(), request.getName(), request.getPower());
    }

    public BatchMagicCityCreateCommand toCommand(BatchMagicCityCreateRequest request) {
        if (request == null) {
            return null;
        }
        return new BatchMagicCityCreateCommand(
                request.getTempId(),
                request.getName(),
                request.getArea(),
                request.getPopulation(),
                request.getEstablishmentDate(),
                request.getGovernor(),
                request.getCapital(),
                request.getPopulationDensity()
        );
    }

    public BatchBookCreatureCreateCommand toCommand(BatchBookCreatureCreateRequest request) {
        if (request == null) {
            return null;
        }
        return new BatchBookCreatureCreateCommand(
                request.getName(),
                coordinatesMapper.toCommand(request.getCoordinates()),
                request.getAge(),
                request.getCreatureType(),
                request.getCreatureLocationId(),
                request.getAttackLevel(),
                request.getDefenseLevel(),
                request.getRingId(),
                request.getTempCreatureLocationId(),
                request.getTempRingId()
        );
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
