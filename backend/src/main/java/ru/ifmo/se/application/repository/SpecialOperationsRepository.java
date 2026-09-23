package ru.ifmo.se.application.repository;

import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.persistence.entity.BookCreature;

public interface SpecialOperationsRepository {
    int deleteByDefenseLevel(float defenseLevel);

    Double calculateAverageDefenseLevel();

    PageResult<BookCreature> findCreaturesWithAttackLevelLessThan(float maxAttackLevel, int page, int size);

    int takeAllRingsFromHobbits();

    int moveHobbitsWithRingsToMordor();
}
