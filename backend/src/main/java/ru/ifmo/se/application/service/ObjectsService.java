package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.command.BatchBookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.BatchMagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.BatchRingCreateCommand;
import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.dto.result.MagicCityResult;
import ru.ifmo.se.application.dto.result.ObjectsResult;
import ru.ifmo.se.application.dto.result.RingResult;
import ru.ifmo.se.application.exception.BatchDuplicateMagicCityTempIdException;
import ru.ifmo.se.application.exception.BatchDuplicateRingTempIdException;
import ru.ifmo.se.application.exception.BatchMagicCityNotFoundException;
import ru.ifmo.se.application.exception.BatchRingNotFoundException;
import ru.ifmo.se.application.mapper.ObjectsMapper;
import ru.ifmo.se.application.usecase.BookCreatureUseCase;
import ru.ifmo.se.application.usecase.MagicCityUseCase;
import ru.ifmo.se.application.usecase.ObjectsUseCase;
import ru.ifmo.se.application.usecase.RingUseCase;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ObjectsService implements ObjectsUseCase {
    @Inject
    private BookCreatureUseCase bookCreatureUseCase;

    @Inject
    private MagicCityUseCase magicCityUseCase;

    @Inject
    private RingUseCase ringUseCase;

    @Inject
    private ObjectsMapper objectsMapper;

    @Override
    @Transactional
    public ObjectsResult create(ObjectsCreateCommand command) {
        ObjectsResult result = new ObjectsResult();

        Map<Integer, Integer> ringRefs = createRings(command.getRings(), result.getRings());
        Map<Integer, Integer> cityRefs = createCities(command.getCities(), result.getCities());
        createCreatures(command.getCreatures(), ringRefs, cityRefs, result.getCreatures());

        return result;
    }

    private Map<Integer, Integer> createRings(List<BatchRingCreateCommand> commands, List<RingResult> results) {
        Map<Integer, Integer> ringRefs = new ConcurrentHashMap<>();
        for (BatchRingCreateCommand command : commands) {
            RingResult createdRing = ringUseCase.create(objectsMapper.toCommand(command));
            results.add(createdRing);
            registerRingReference(ringRefs, command.getTempId(), createdRing.getId());
        }
        return ringRefs;
    }

    private Map<Integer, Integer> createCities(List<BatchMagicCityCreateCommand> commands, List<MagicCityResult> results) {
        Map<Integer, Integer> cityRefs = new ConcurrentHashMap<>();
        for (BatchMagicCityCreateCommand command : commands) {
            MagicCityResult createdCity = magicCityUseCase.create(objectsMapper.toCommand(command));
            results.add(createdCity);
            registerCityReference(cityRefs, command.getTempId(), createdCity.getId());
        }
        return cityRefs;
    }

    private void registerRingReference(Map<Integer, Integer> refs, Integer tempId, Integer id) {
        if (tempId != null && refs.putIfAbsent(tempId, id) != null) {
            throw new BatchDuplicateRingTempIdException(tempId);
        }
    }

    private void registerCityReference(Map<Integer, Integer> refs, Integer tempId, Integer id) {
        if (tempId != null && refs.putIfAbsent(tempId, id) != null) {
            throw new BatchDuplicateMagicCityTempIdException(tempId);
        }
    }

    private void createCreatures(List<BatchBookCreatureCreateCommand> commands,
                                 Map<Integer, Integer> ringRefs,
                                 Map<Integer, Integer> cityRefs,
                                 List<BookCreatureResult> results) {
        for (BatchBookCreatureCreateCommand creatureCommand : commands) {
            resolveRingReference(creatureCommand, ringRefs);
            resolveCityReference(creatureCommand, cityRefs);
            results.add(bookCreatureUseCase.create(objectsMapper.toCommand(creatureCommand)));
        }
    }

    private void resolveRingReference(BatchBookCreatureCreateCommand creatureCommand, Map<Integer, Integer> ringRefs) {
        Integer tempRingId = creatureCommand.getTempRingId();
        if (tempRingId != null) {
            Integer resolvedRingId = ringRefs.get(tempRingId);
            if (resolvedRingId == null) {
                throw new BatchRingNotFoundException(tempRingId);
            }
            creatureCommand.setRingId(resolvedRingId);
        }
    }

    private void resolveCityReference(BatchBookCreatureCreateCommand creatureCommand, Map<Integer, Integer> cityRefs) {
        Integer tempCityId = creatureCommand.getTempCreatureLocationId();
        if (tempCityId != null) {
            Integer resolvedCityId = cityRefs.get(tempCityId);
            if (resolvedCityId == null) {
                throw new BatchMagicCityNotFoundException(tempCityId);
            }
            creatureCommand.setCreatureLocationId(resolvedCityId);
        }
    }
}
