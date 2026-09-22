package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.result.BookCreatureResult;

import java.util.List;

public interface SpecialOperationsUseCase {
    int deleteByDefenseLevel(float defenseLevel);

    Double calculateAverageDefenseLevel();

    List<BookCreatureResult> findCreaturesWithAttackLevelLessThan(float maxAttackLevel);

    int takeAllRingsFromHobbits();

    int moveHobbitsWithRingsToMordor();
}
