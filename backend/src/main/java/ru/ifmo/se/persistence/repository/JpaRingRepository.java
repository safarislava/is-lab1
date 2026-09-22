package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.application.repository.RingRepository;
import ru.ifmo.se.persistence.entity.Ring;
import ru.ifmo.se.persistence.interceptor.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaRingRepository implements RingRepository {
    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional
    public Ring save(Ring ring) {
        if (ring.getId() == 0) {
            entityManager.persist(ring);
            return ring;
        }
        return entityManager.merge(ring);
    }

    @Override
    public Optional<Ring> findById(int id) {
        return Optional.ofNullable(entityManager.find(Ring.class, id));
    }

    @Override
    public List<Ring> findAll() {
        return entityManager.createQuery("SELECT r FROM Ring r", Ring.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Ring ring = entityManager.find(Ring.class, id);
        if (ring != null) {
            entityManager.remove(ring);
        }
    }
}
