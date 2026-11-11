package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.command.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.command.MagicCityUpdateCommand;
import ru.ifmo.se.application.dto.event.NotificationEvent;
import ru.ifmo.se.application.dto.result.MagicCityResult;
import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.application.enums.EntityType;
import ru.ifmo.se.application.exception.MagicCityNotFoundException;
import ru.ifmo.se.application.exception.MagicCitySwapSameException;
import ru.ifmo.se.application.mapper.MagicCityMapper;
import ru.ifmo.se.application.repository.BookCreatureRepository;
import ru.ifmo.se.application.repository.MagicCityRepository;
import ru.ifmo.se.application.usecase.MagicCityUseCase;
import ru.ifmo.se.persistence.entity.MagicCity;

import java.util.List;

@ApplicationScoped
public class MagicCityService implements MagicCityUseCase {
    @Inject
    private MagicCityRepository magicCityRepository;

    @Inject
    private BookCreatureRepository bookCreatureRepository;

    @Inject
    private MagicCityMapper magicCityMapper;

    @Inject
    private Event<NotificationEvent> eventPublisher;

    @Override
    @Transactional
    public MagicCityResult create(MagicCityCreateCommand command) {
        MagicCity city = magicCityMapper.toEntity(command);
        MagicCity saved = magicCityRepository.save(city);
        eventPublisher.fire(new NotificationEvent(ActionType.CREATE, EntityType.MAGIC_CITY, saved.getId()));
        return magicCityMapper.toResult(saved);
    }

    @Override
    public MagicCityResult getById(int id) {
        MagicCity city = magicCityRepository.findById(id)
                .orElseThrow(() -> new MagicCityNotFoundException(id));
        return magicCityMapper.toResult(city);
    }

    @Override
    @Transactional
    public MagicCityResult update(int id, MagicCityUpdateCommand command) {
        MagicCity city = magicCityRepository.findById(id)
                .orElseThrow(() -> new MagicCityNotFoundException(id));
        magicCityMapper.updateEntity(city, command);
        MagicCity saved = magicCityRepository.save(city);
        eventPublisher.fire(new NotificationEvent(ActionType.UPDATE, EntityType.MAGIC_CITY, saved.getId()));
        return magicCityMapper.toResult(saved);
    }

    @Override
    @Transactional
    public void delete(int id, int replacementCityId) {
        if (replacementCityId == id) {
            throw new MagicCitySwapSameException(id);
        }
        if (magicCityRepository.findById(id).isEmpty()) {
            throw new MagicCityNotFoundException(id);
        }
        MagicCity replacementCity = magicCityRepository.findById(replacementCityId)
                .orElseThrow(() -> new MagicCityNotFoundException(replacementCityId));
        bookCreatureRepository.reassignCity(id, replacementCity);
        magicCityRepository.deleteById(id);
        eventPublisher.fire(new NotificationEvent(ActionType.DELETE, EntityType.MAGIC_CITY, id));
    }

    @Override
    public List<MagicCityResult> getAll() {
        return magicCityRepository.findAll().stream()
                .map(magicCityMapper::toResult)
                .toList();
    }
}
