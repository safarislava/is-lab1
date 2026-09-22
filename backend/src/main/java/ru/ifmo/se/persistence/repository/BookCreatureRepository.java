package ru.ifmo.se.persistence.repository;

import ru.ifmo.se.persistence.entity.BookCreature;

import java.util.Optional;

public interface BookCreatureRepository {
    BookCreature save(BookCreature bookCreature);

    Optional<BookCreature> findById(int id);

    Optional<BookCreature> findByRingId(Integer ringId);

    void deleteById(int id);
}
