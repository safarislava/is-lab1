package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.command.UpdateUserRolesCommand;
import ru.ifmo.se.application.dto.result.UserResult;

import java.util.List;

public interface AdminUseCase {
    List<UserResult> getAllUsers();

    UserResult updateUserRoles(UpdateUserRolesCommand command);
}
