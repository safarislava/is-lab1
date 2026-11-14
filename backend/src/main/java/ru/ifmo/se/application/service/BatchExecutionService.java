package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import ru.ifmo.se.application.dto.command.BatchBookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.BatchMagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.BatchRingCreateCommand;
import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.dto.result.MagicCityResult;
import ru.ifmo.se.application.dto.result.RingResult;
import ru.ifmo.se.application.exception.BatchDuplicateMagicCityTempIdException;
import ru.ifmo.se.application.exception.BatchDuplicateRingTempIdException;
import ru.ifmo.se.application.exception.BatchMagicCityNotFoundException;
import ru.ifmo.se.application.exception.BatchRingNotFoundException;
import ru.ifmo.se.application.mapper.ObjectsMapper;
import ru.ifmo.se.application.usecase.BookCreatureUseCase;
import ru.ifmo.se.application.usecase.MagicCityUseCase;
import ru.ifmo.se.application.usecase.RingUseCase;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class BatchExecutionService {

    @Inject
    private BookCreatureUseCase bookCreatureUseCase;

    @Inject
    private MagicCityUseCase magicCityUseCase;

    @Inject
    private RingUseCase ringUseCase;

    @Inject
    private ObjectsMapper objectsMapper;

    @Inject
    private Validator validator;

    public void validateCommand(ObjectsCreateCommand command) {
        if (command == null) {
            throw new ValidationException("Пакет импорта не может быть пустым");
        }

        Set<ConstraintViolation<ObjectsCreateCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Map<Integer, Integer> createRings(List<BatchRingCreateCommand> commands, List<RingResult> results) {
        Map<Integer, Integer> ringRefs = new ConcurrentHashMap<>();
        for (BatchRingCreateCommand command : commands) {
            RingResult createdRing = ringUseCase.create(objectsMapper.toCommand(command));
            results.add(createdRing);
            registerRingReference(ringRefs, command.getTempId(), createdRing.getId());
        }
        return ringRefs;
    }

    public Map<Integer, Integer> createCities(List<BatchMagicCityCreateCommand> commands, List<MagicCityResult> results) {
        Map<Integer, Integer> cityRefs = new ConcurrentHashMap<>();
        for (BatchMagicCityCreateCommand command : commands) {
            MagicCityResult createdCity = magicCityUseCase.create(objectsMapper.toCommand(command));
            results.add(createdCity);
            registerCityReference(cityRefs, command.getTempId(), createdCity.getId());
        }
        return cityRefs;
    }

    public void createCreatures(List<BatchBookCreatureCreateCommand> commands,
                                Map<Integer, Integer> ringRefs,
                                Map<Integer, Integer> cityRefs,
                                List<BookCreatureResult> results) {
        for (BatchBookCreatureCreateCommand creatureCommand : commands) {
            resolveRingReference(creatureCommand, ringRefs);
            resolveCityReference(creatureCommand, cityRefs);
            results.add(bookCreatureUseCase.create(objectsMapper.toCommand(creatureCommand)));
        }
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
