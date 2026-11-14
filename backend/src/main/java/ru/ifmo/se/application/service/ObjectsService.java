package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.event.ImportEvent;
import ru.ifmo.se.application.dto.result.ObjectsResult;
import ru.ifmo.se.application.usecase.ObjectsUseCase;

import java.util.Map;

@ApplicationScoped
public class ObjectsService implements ObjectsUseCase {

    @Inject
    private Event<ImportEvent> importEventPublisher;

    @Inject
    private BatchExecutionService batchExecutionService;

    @Override
    @Transactional
    public ObjectsResult create(ObjectsCreateCommand command, String username) {
        ImportEvent startEvent = ImportEvent.start(username);
        importEventPublisher.fire(startEvent);

        batchExecutionService.validateCommand(command);

        ObjectsResult result = new ObjectsResult();

        Map<Integer, Integer> ringRefs = batchExecutionService.createRings(command.getRings(), result.getRings());
        Map<Integer, Integer> cityRefs = batchExecutionService.createCities(command.getCities(), result.getCities());
        batchExecutionService.createCreatures(command.getCreatures(), ringRefs, cityRefs, result.getCreatures());

        int count = result.getRings().size() + result.getCities().size() + result.getCreatures().size();
        importEventPublisher.fire(ImportEvent.complete(startEvent.getOperationId(), count));

        return result;
    }
}
