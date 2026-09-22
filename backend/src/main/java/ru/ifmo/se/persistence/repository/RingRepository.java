package ru.ifmo.se.persistence.repository;

import ru.ifmo.se.persistence.entity.Ring;

import java.util.List;
import java.util.Optional;

public interface RingRepository {
    Ring save(Ring ring);

    Optional<Ring> findById(int id);

    List<Ring> findAll();

    void deleteById(int id);
}
