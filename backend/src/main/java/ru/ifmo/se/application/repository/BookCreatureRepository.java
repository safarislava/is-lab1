package ru.ifmo.se.application.repository;

import ru.ifmo.se.application.dto.request.CreatureSearchQuery;
import ru.ifmo.se.application.dto.response.PageResponse;
import ru.ifmo.se.persistence.entity.BookCreature;
import ru.ifmo.se.persistence.entity.MagicCity;

import java.util.Optional;

public interface BookCreatureRepository {
    BookCreature save(BookCreature bookCreature);

    Optional<BookCreature> findById(int id);

    Optional<BookCreature> findByRingId(Integer ringId);

    void reassignCity(int oldCityId, MagicCity newCity);

    void deleteById(int id);

    PageResponse<BookCreature> findBySearchQuery(CreatureSearchQuery query);
}
