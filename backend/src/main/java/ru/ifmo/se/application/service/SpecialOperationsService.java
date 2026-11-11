package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.event.NotificationEvent;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.application.enums.EntityType;
import ru.ifmo.se.application.mapper.BookCreatureMapper;
import ru.ifmo.se.application.repository.SpecialOperationsRepository;
import ru.ifmo.se.application.usecase.SpecialOperationsUseCase;
import ru.ifmo.se.persistence.entity.BookCreature;

import java.util.List;

@ApplicationScoped
public class SpecialOperationsService implements SpecialOperationsUseCase {
    @Inject
    private SpecialOperationsRepository specialOperationsRepository;

    @Inject
    private BookCreatureMapper bookCreatureMapper;

    @Inject
    private Event<NotificationEvent> eventPublisher;

    @Override
    @Transactional
    public int deleteByDefenseLevel(float defenseLevel) {
        int deletedCount = specialOperationsRepository.deleteByDefenseLevel(defenseLevel);
        if (deletedCount > 0) {
            eventPublisher.fire(new NotificationEvent(ActionType.DELETE, EntityType.BOOK_CREATURE, 0));
        }
        return deletedCount;
    }

    @Override
    public Double calculateAverageDefenseLevel() {
        return specialOperationsRepository.calculateAverageDefenseLevel();
    }

    @Override
    public PageResult<BookCreatureResult> findCreaturesWithAttackLevelLessThan(float maxAttackLevel, int page, int size) {
        PageResult<BookCreature> pageResult = specialOperationsRepository.findCreaturesWithAttackLevelLessThan(maxAttackLevel, page, size);
        List<BookCreatureResult> content = pageResult.getContent().stream()
                .map(bookCreatureMapper::toResult)
                .toList();
        return new PageResult<>(
                content,
                pageResult.getTotal(),
                pageResult.getPage(),
                pageResult.getSize()
        );
    }

    @Override
    @Transactional
    public int takeAllRingsFromHobbits() {
        int updatedCount = specialOperationsRepository.takeAllRingsFromHobbits();
        if (updatedCount > 0) {
            eventPublisher.fire(new NotificationEvent(ActionType.UPDATE, EntityType.BOOK_CREATURE, 0));
        }
        return updatedCount;
    }

    @Override
    @Transactional
    public int moveHobbitsWithRingsToMordor() {
        int movedCount = specialOperationsRepository.moveHobbitsWithRingsToMordor();
        if (movedCount > 0) {
            eventPublisher.fire(new NotificationEvent(ActionType.UPDATE, EntityType.BOOK_CREATURE, 0));
        }
        return movedCount;
    }
}
