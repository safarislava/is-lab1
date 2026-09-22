package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.response.BookCreatureResponse;

import java.util.List;

public interface SpecialOperationsUseCase {
    int deleteByDefenseLevel(float defenseLevel);

    Double calculateAverageDefenseLevel();

    List<BookCreatureResponse> findCreaturesWithAttackLevelLessThan(float maxAttackLevel);

    int takeAllRingsFromHobbits();

    int moveHobbitsWithRingsToMordor();
}
