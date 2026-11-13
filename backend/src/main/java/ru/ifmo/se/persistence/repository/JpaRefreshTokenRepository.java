package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.ifmo.se.application.repository.RefreshTokenRepository;
import ru.ifmo.se.persistence.entity.RefreshToken;
import ru.ifmo.se.persistence.entity.User;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaRefreshTokenRepository implements RefreshTokenRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        if (refreshToken.getId() == 0) {
            entityManager.persist(refreshToken);
            return refreshToken;
        }
        return entityManager.merge(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        List<RefreshToken> results = entityManager.createQuery(
                "SELECT r FROM RefreshToken r WHERE r.token = :token", RefreshToken.class)
                .setParameter("token", token)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public void revokeByUser(User user) {
        entityManager.createQuery(
                "UPDATE RefreshToken r SET r.revoked = true WHERE r.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }

    @Override
    public void deleteByToken(String token) {
        entityManager.createQuery(
                "DELETE FROM RefreshToken r WHERE r.token = :token")
                .setParameter("token", token)
                .executeUpdate();
    }
}
