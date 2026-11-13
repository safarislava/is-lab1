package ru.ifmo.se.application.repository;

import ru.ifmo.se.persistence.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(int id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAll();
}
