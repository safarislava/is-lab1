package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.ifmo.se.application.repository.UserRepository;
import ru.ifmo.se.persistence.entity.User;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        if (user.getId() == 0) {
            entityManager.persist(user);
            return user;
        }
        return entityManager.merge(user);
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> results = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public boolean existsByUsername(String username) {
        Long count = entityManager.createQuery(
                "SELECT count(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count != null && count > 0;
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }
}
