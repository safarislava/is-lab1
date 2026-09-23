package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.application.repository.MagicCityRepository;
import ru.ifmo.se.persistence.entity.MagicCity;
import ru.ifmo.se.persistence.interceptor.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaMagicCityRepository implements MagicCityRepository {
    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional
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
    @Transactional
    public void deleteById(int id) {
        MagicCity city = entityManager.find(MagicCity.class, id);
        if (city != null) {
            entityManager.remove(city);
        }
    }
}
