package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.ifmo.se.application.repository.MagicCityRepository;
import ru.ifmo.se.persistence.entity.MagicCity;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaMagicCityRepository implements MagicCityRepository {
    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    public MagicCity save(MagicCity magicCity) {
        if (magicCity.getId() == 0) {
            entityManager.persist(magicCity);
            return magicCity;
        }
        return entityManager.merge(magicCity);
    }

    @Override
    public Optional<MagicCity> findById(int id) {
        return Optional.ofNullable(entityManager.find(MagicCity.class, id));
    }

    @Override
    public List<MagicCity> findAll() {
        return entityManager.createQuery("SELECT c FROM MagicCity c", MagicCity.class)
                .getResultList();
    }

    @Override
    public void deleteById(int id) {
        MagicCity city = entityManager.find(MagicCity.class, id);
        if (city != null) {
            entityManager.remove(city);
        }
    }
}
