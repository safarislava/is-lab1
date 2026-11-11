package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.command.RingCreateCommand;
import ru.ifmo.se.application.dto.command.RingUpdateCommand;
import ru.ifmo.se.application.dto.event.NotificationEvent;
import ru.ifmo.se.application.dto.result.RingResult;
import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.application.enums.EntityType;
import ru.ifmo.se.application.exception.RingNotFoundException;
import ru.ifmo.se.application.mapper.RingMapper;
import ru.ifmo.se.application.repository.RingRepository;
import ru.ifmo.se.application.usecase.RingUseCase;
import ru.ifmo.se.persistence.entity.Ring;

import java.util.List;

@ApplicationScoped
public class RingService implements RingUseCase {
    @Inject
    private RingRepository ringRepository;

    @Inject
    private RingMapper ringMapper;

    @Inject
    private Event<NotificationEvent> eventPublisher;

    @Override
    @Transactional
    public RingResult create(RingCreateCommand command) {
        Ring ring = ringMapper.toEntity(command);
        Ring saved = ringRepository.save(ring);
        eventPublisher.fire(new NotificationEvent(ActionType.CREATE, EntityType.RING, saved.getId()));
        return ringMapper.toResult(saved);
    }

    @Override
    public RingResult getById(int id) {
        Ring ring = ringRepository.findById(id)
                .orElseThrow(() -> new RingNotFoundException(id));
        return ringMapper.toResult(ring);
    }

    @Override
    @Transactional
    public RingResult update(int id, RingUpdateCommand command) {
        Ring ring = ringRepository.findById(id)
                .orElseThrow(() -> new RingNotFoundException(id));
        ringMapper.updateEntity(ring, command);
        Ring saved = ringRepository.save(ring);
        eventPublisher.fire(new NotificationEvent(ActionType.UPDATE, EntityType.RING, saved.getId()));
        return ringMapper.toResult(saved);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (ringRepository.findById(id).isEmpty()) {
            throw new RingNotFoundException(id);
        }
        ringRepository.deleteById(id);
        eventPublisher.fire(new NotificationEvent(ActionType.DELETE, EntityType.RING, id));
    }

    @Override
    public List<RingResult> getAll() {
        return ringRepository.findAll().stream()
                .map(ringMapper::toResult)
                .toList();
    }
}
