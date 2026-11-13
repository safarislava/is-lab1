package ru.ifmo.se.application.repository;

import ru.ifmo.se.persistence.entity.RefreshToken;
import ru.ifmo.se.persistence.entity.User;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    void revokeByUser(User user);

    void deleteByToken(String token);
}
