package ru.ifmo.se.persistence.repository;

import ru.ifmo.se.persistence.entity.MagicCity;

import java.util.List;
import java.util.Optional;

public interface MagicCityRepository {
    MagicCity save(MagicCity magicCity);

    Optional<MagicCity> findById(int id);

    List<MagicCity> findAll();

    void deleteById(int id);
}
