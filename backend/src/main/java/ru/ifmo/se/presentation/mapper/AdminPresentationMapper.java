package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.command.UpdateUserRolesCommand;
import ru.ifmo.se.application.dto.result.UserResult;
import ru.ifmo.se.presentation.dto.request.UpdateUserRolesRequest;
import ru.ifmo.se.presentation.dto.response.UserResponse;

@ApplicationScoped
public class AdminPresentationMapper {

    @Inject
    private AuthPresentationMapper authPresentationMapper;

    public UpdateUserRolesCommand toCommand(int userId, UpdateUserRolesRequest request) {
        if (request == null) {
            return null;
        }
        return new UpdateUserRolesCommand(userId, request.getRoles());
    }

    public UserResponse toResponse(UserResult result) {
        return authPresentationMapper.toResponse(result);
    }
}
