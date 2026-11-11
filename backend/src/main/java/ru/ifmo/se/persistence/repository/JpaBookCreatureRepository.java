package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.ifmo.se.application.dto.query.CreatureSearchQuery;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.repository.BookCreatureRepository;
import ru.ifmo.se.persistence.entity.BookCreature;
import ru.ifmo.se.persistence.entity.MagicCity;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaBookCreatureRepository implements BookCreatureRepository {
    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    public BookCreature save(BookCreature bookCreature) {
        if (bookCreature.getId() == 0) {
            entityManager.persist(bookCreature);
            return bookCreature;
        }
        return entityManager.merge(bookCreature);
    }

    @Override
    public Optional<BookCreature> findById(int id) {
        return Optional.ofNullable(entityManager.find(BookCreature.class, id));
    }

    @Override
    public Optional<BookCreature> findByRingId(Integer ringId) {
        if (ringId == null) {
            return Optional.empty();
        }
        List<BookCreature> results = entityManager.createQuery(
                "SELECT b FROM BookCreature b WHERE b.ring.id = :ringId", BookCreature.class)
                .setParameter("ringId", ringId)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public void reassignCity(int oldCityId, MagicCity newCity) {
        entityManager.createQuery(
                "UPDATE BookCreature b SET b.creatureLocation = :newCity WHERE b.creatureLocation.id = :oldCityId")
                .setParameter("newCity", newCity)
                .setParameter("oldCityId", oldCityId)
                .executeUpdate();
    }

    @Override
    public void deleteById(int id) {
        BookCreature creature = entityManager.find(BookCreature.class, id);
        if (creature != null) {
            entityManager.remove(creature);
        }
    }

    @Override
    public PageResult<BookCreature> findBySearchQuery(CreatureSearchQuery query) {
        return new BookCreatureSearch(entityManager, query).execute();
    }
}
