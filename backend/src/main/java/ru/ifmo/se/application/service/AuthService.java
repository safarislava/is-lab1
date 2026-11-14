package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.command.LoginCommand;
import ru.ifmo.se.application.dto.command.RegisterCommand;
import ru.ifmo.se.application.dto.result.AuthTokensResult;
import ru.ifmo.se.application.dto.result.UserResult;
import ru.ifmo.se.application.exception.ExpiredRefreshTokenException;
import ru.ifmo.se.application.exception.InvalidCredentialsException;
import ru.ifmo.se.application.exception.InvalidRefreshTokenException;
import ru.ifmo.se.application.exception.MissingRefreshTokenException;
import ru.ifmo.se.application.exception.UserAlreadyExistsException;
import ru.ifmo.se.application.exception.UserNotFoundException;
import ru.ifmo.se.application.mapper.AuthMapper;
import ru.ifmo.se.application.repository.RefreshTokenRepository;
import ru.ifmo.se.application.repository.UserRepository;
import ru.ifmo.se.application.usecase.AuthUseCase;
import ru.ifmo.se.persistence.entity.RefreshToken;
import ru.ifmo.se.persistence.entity.RoleType;
import ru.ifmo.se.persistence.entity.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class AuthService implements AuthUseCase {
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 7;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RefreshTokenRepository refreshTokenRepository;

    @Inject
    private PasswordService passwordService;

    @Inject
    private JwtService jwtService;

    @Inject
    private AuthMapper authMapper;

    @Override
    @Transactional
    public UserResult register(RegisterCommand command) {
        if (userRepository.existsByUsername(command.getUsername())) {
            throw new UserAlreadyExistsException(command.getUsername());
        }

        String passwordHash = passwordService.hashPassword(command.getPassword());

        User user = new User();
        user.setUsername(command.getUsername());
        user.setPasswordHash(passwordHash);
        Set<RoleType> roles = command.getUsername().startsWith("admin")
                ? EnumSet.of(RoleType.USER, RoleType.ADMIN)
                : EnumSet.of(RoleType.USER);
        user.setRoles(roles);

        User saved = userRepository.save(user);
        return authMapper.toResult(saved);
    }

    @Override
    @Transactional
    public AuthTokensResult login(LoginCommand command) {
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordService.verifyPassword(command.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return createAuthTokens(user);
    }

    @Override
    @Transactional
    public AuthTokensResult refreshToken(String refreshTokenValue) {
        if (refreshTokenValue == null || refreshTokenValue.isBlank()) {
            throw new MissingRefreshTokenException();
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteByToken(refreshTokenValue);
            throw new ExpiredRefreshTokenException();
        }

        User user = refreshToken.getUser();
        
        refreshTokenRepository.deleteByToken(refreshTokenValue);

        return createAuthTokens(user);
    }

    @Override
    @Transactional
    public void logout(String refreshTokenValue) {
        if (refreshTokenValue != null && !refreshTokenValue.isBlank()) {
            refreshTokenRepository.deleteByToken(refreshTokenValue);
        }
    }

    @Override
    public UserResult getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return authMapper.toResult(user);
    }

    private AuthTokensResult createAuthTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getUsername(), user.getRoles());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_VALIDITY_DAYS, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);

        RefreshToken savedRefresh = refreshTokenRepository.save(refreshToken);

        return new AuthTokensResult(
                accessToken,
                savedRefresh.getToken(),
                user.getUsername(),
                user.getRoles()
        );
    }
}
