package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.command.LoginCommand;
import ru.ifmo.se.application.dto.command.RegisterCommand;
import ru.ifmo.se.application.dto.result.AuthTokensResult;
import ru.ifmo.se.application.dto.result.UserResult;
import ru.ifmo.se.persistence.entity.RoleType;
import ru.ifmo.se.presentation.dto.request.LoginRequest;
import ru.ifmo.se.presentation.dto.request.RegisterRequest;
import ru.ifmo.se.presentation.dto.response.AuthResponse;
import ru.ifmo.se.presentation.dto.response.UserResponse;

import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthPresentationMapper {

    public RegisterCommand toCommand(RegisterRequest request) {
        if (request == null) {
            return null;
        }
        return new RegisterCommand(request.getUsername(), request.getPassword());
    }

    public LoginCommand toCommand(LoginRequest request) {
        if (request == null) {
            return null;
        }
        return new LoginCommand(request.getUsername(), request.getPassword());
    }

    public UserResponse toResponse(UserResult result) {
        if (result == null) {
            return null;
        }
        Set<String> roleNames = result.getRoles().stream()
                .map(RoleType::name)
                .collect(Collectors.toSet());
        return new UserResponse(result.getId(), result.getUsername(), roleNames);
    }

    public AuthResponse toResponse(AuthTokensResult result, String message) {
        if (result == null) {
            return null;
        }
        Set<String> roleNames = result.getRoles().stream()
                .map(RoleType::name)
                .collect(Collectors.toSet());
        return new AuthResponse(message, result.getUsername(), roleNames);
    }
}
