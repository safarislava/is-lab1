package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import ru.ifmo.se.application.repository.BookCreatureRepository;
import ru.ifmo.se.persistence.entity.BookCreature;
import ru.ifmo.se.persistence.interceptor.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaBookCreatureRepository implements BookCreatureRepository {
    private static final String FIELD_ID = "id";
    private static final String FIELD_RING = "ring";

    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional
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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookCreature> cq = cb.createQuery(BookCreature.class);
        Root<BookCreature> root = cq.from(BookCreature.class);
        cq.select(root).where(cb.equal(root.get(FIELD_RING).get(FIELD_ID), ringId));
        List<BookCreature> results = entityManager.createQuery(cq).getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        BookCreature creature = entityManager.find(BookCreature.class, id);
        if (creature != null) {
            entityManager.remove(creature);
        }
    }
}
