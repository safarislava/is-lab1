package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.repository.SpecialOperationsRepository;
import ru.ifmo.se.persistence.entity.BookCreature;
import ru.ifmo.se.persistence.interceptor.Transactional;

import java.util.List;

@ApplicationScoped
public class JpaSpecialOperationsRepository implements SpecialOperationsRepository {
    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional
    public int deleteByDefenseLevel(float defenseLevel) {
        Number result = (Number) entityManager.createNativeQuery(
                "SELECT delete_by_defense_level(?1)")
                .setParameter(1, defenseLevel)
                .getSingleResult();
        return result.intValue();
    }

    @Override
    public Double calculateAverageDefenseLevel() {
        Object result = entityManager.createNativeQuery(
                "SELECT calculate_avg_defense_level()")
                .getSingleResult();
        return result != null ? ((Number) result).doubleValue() : 0.0;
    }

    @Override
    public PageResult<BookCreature> findCreaturesWithAttackLevelLessThan(float maxAttackLevel, int page, int size) {
        Long total = entityManager.createQuery(
                "SELECT COUNT(b) FROM BookCreature b WHERE b.attackLevel < :maxAttackLevel", Long.class)
                .setParameter("maxAttackLevel", maxAttackLevel)
                .getSingleResult();
        List<BookCreature> content = entityManager.createQuery(
                "SELECT b FROM BookCreature b WHERE b.attackLevel < :maxAttackLevel ORDER BY b.id", BookCreature.class)
                .setParameter("maxAttackLevel", maxAttackLevel)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
        return new PageResult<>(content, total, page, size);
    }

    @Override
    @Transactional
    public int takeAllRingsFromHobbits() {
        Number result = (Number) entityManager.createNativeQuery(
                "SELECT take_rings_from_hobbits()")
                .getSingleResult();
        return result.intValue();
    }

    @Override
    @Transactional
    public int moveHobbitsWithRingsToMordor() {
        Number result = (Number) entityManager.createNativeQuery(
                "SELECT move_hobbits_with_rings_to_mordor()")
                .getSingleResult();
        return result.intValue();
    }
}
