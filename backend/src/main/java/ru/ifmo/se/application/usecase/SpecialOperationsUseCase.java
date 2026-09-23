package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.dto.result.PageResult;

public interface SpecialOperationsUseCase {
    int deleteByDefenseLevel(float defenseLevel);

    Double calculateAverageDefenseLevel();

    PageResult<BookCreatureResult> findCreaturesWithAttackLevelLessThan(float maxAttackLevel, int page, int size);

    int takeAllRingsFromHobbits();

    int moveHobbitsWithRingsToMordor();
}
