package ru.ifmo.se.application.repository;

import ru.ifmo.se.persistence.entity.BookCreature;

import java.util.List;

public interface SpecialOperationsRepository {
    int deleteByDefenseLevel(float defenseLevel);

    Double calculateAverageDefenseLevel();

    List<BookCreature> findCreaturesWithAttackLevelLessThan(float maxAttackLevel);

    int takeAllRingsFromHobbits();

    int moveHobbitsWithRingsToMordor();
}
