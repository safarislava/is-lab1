package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.ifmo.se.application.repository.RingRepository;
import ru.ifmo.se.persistence.entity.Ring;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaRingRepository implements RingRepository {
    @PersistenceContext()
    private EntityManager entityManager;

    @Override
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
    public void deleteById(int id) {
        entityManager.createNativeQuery("SELECT delete_ring_with_detach(:ring)")
            .setParameter("ring", id)
            .getSingleResult();
    }
}
