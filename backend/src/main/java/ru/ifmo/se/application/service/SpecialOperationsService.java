package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.application.enums.EntityType;
import ru.ifmo.se.application.mapper.BookCreatureMapper;
import ru.ifmo.se.application.repository.SpecialOperationsRepository;
import ru.ifmo.se.application.usecase.NotificationBroadcastUseCase;
import ru.ifmo.se.application.usecase.SpecialOperationsUseCase;

import java.util.List;

@ApplicationScoped
public class SpecialOperationsService implements SpecialOperationsUseCase {
    @Inject
    private SpecialOperationsRepository specialOperationsRepository;

    @Inject
    private BookCreatureMapper bookCreatureMapper;

    @Inject
    private NotificationBroadcastUseCase notificationBroadcastUseCase;

    @Override
    public int deleteByDefenseLevel(float defenseLevel) {
        int deletedCount = specialOperationsRepository.deleteByDefenseLevel(defenseLevel);
        if (deletedCount > 0) {
            notificationBroadcastUseCase.broadcastChange(ActionType.DELETE, EntityType.BOOK_CREATURE, 0);
        }
        return deletedCount;
    }

    @Override
    public Double calculateAverageDefenseLevel() {
        return specialOperationsRepository.calculateAverageDefenseLevel();
    }

    @Override
    public List<BookCreatureResult> findCreaturesWithAttackLevelLessThan(float maxAttackLevel) {
        return specialOperationsRepository.findCreaturesWithAttackLevelLessThan(maxAttackLevel)
                .stream()
                .map(bookCreatureMapper::toResult)
                .toList();
    }

    @Override
    public int takeAllRingsFromHobbits() {
        int updatedCount = specialOperationsRepository.takeAllRingsFromHobbits();
        if (updatedCount > 0) {
            notificationBroadcastUseCase.broadcastChange(ActionType.UPDATE, EntityType.BOOK_CREATURE, 0);
        }
        return updatedCount;
    }

    @Override
    public int moveHobbitsWithRingsToMordor() {
        int movedCount = specialOperationsRepository.moveHobbitsWithRingsToMordor();
        if (movedCount > 0) {
            notificationBroadcastUseCase.broadcastChange(ActionType.UPDATE, EntityType.BOOK_CREATURE, 0);
        }
        return movedCount;
    }
}
