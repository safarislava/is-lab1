package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.command.UpdateUserRolesCommand;
import ru.ifmo.se.application.dto.result.UserResult;
import ru.ifmo.se.application.exception.UserNotFoundException;
import ru.ifmo.se.application.mapper.AuthMapper;
import ru.ifmo.se.application.repository.RefreshTokenRepository;
import ru.ifmo.se.application.repository.UserRepository;
import ru.ifmo.se.application.usecase.AdminUseCase;
import ru.ifmo.se.persistence.entity.RoleType;
import ru.ifmo.se.persistence.entity.User;

import java.util.EnumSet;
import java.util.List;

@ApplicationScoped
public class AdminService implements AdminUseCase {

    @Inject
    private UserRepository userRepository;

    @Inject
    private RefreshTokenRepository refreshTokenRepository;

    @Inject
    private AuthMapper authMapper;

    @Override
    public List<UserResult> getAllUsers() {
        return userRepository.findAll().stream()
                .map(authMapper::toResult)
                .toList();
    }

    @Override
    @Transactional
    public UserResult updateUserRoles(UpdateUserRolesCommand command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new UserNotFoundException(command.getUserId()));

        user.setRoles(command.getRoles().isEmpty()
                ? EnumSet.noneOf(RoleType.class)
                : EnumSet.copyOf(command.getRoles()));
        User saved = userRepository.save(user);

        refreshTokenRepository.revokeByUser(saved);

        return authMapper.toResult(saved);
    }
}
