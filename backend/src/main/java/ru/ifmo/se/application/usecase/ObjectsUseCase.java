package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.command.ObjectsCreateCommand;
import ru.ifmo.se.application.dto.result.ObjectsResult;

public interface ObjectsUseCase {
    ObjectsResult create(ObjectsCreateCommand command, String username);
}
