package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.command.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.command.RingCreateCommand;
import ru.ifmo.se.application.dto.result.ObjectsResult;
import ru.ifmo.se.application.usecase.BookCreatureUseCase;
import ru.ifmo.se.application.usecase.MagicCityUseCase;
import ru.ifmo.se.application.usecase.ObjectsUseCase;
import ru.ifmo.se.application.usecase.RingUseCase;

@ApplicationScoped
public class ObjectsService implements ObjectsUseCase {
    @Inject
    private BookCreatureUseCase bookCreatureUseCase;

    @Inject
    private MagicCityUseCase magicCityUseCase;

    @Inject
    private RingUseCase ringUseCase;

    @Override
    @Transactional
    public ObjectsResult create(ObjectsCreateCommand command) {
        ObjectsResult result = new ObjectsResult();
        for (RingCreateCommand ringCreateCommand : command.getRings()) {
            result.getRings().add(ringUseCase.create(ringCreateCommand));
        }
        for (MagicCityCreateCommand magicCityCreateCommand : command.getCities()) {
            result.getCities().add(magicCityUseCase.create(magicCityCreateCommand));
        }
        for (BookCreatureCreateCommand bookCreatureCreateCommand : command.getCreatures()) {
            result.getCreatures().add(bookCreatureUseCase.create(bookCreatureCreateCommand));
        }
        return result;
    }
}
