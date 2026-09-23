package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.command.RingCreateCommand;
import ru.ifmo.se.application.dto.command.RingUpdateCommand;
import ru.ifmo.se.application.dto.result.RingResult;
import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.application.enums.EntityType;
import ru.ifmo.se.application.exception.RingNotFoundException;
import ru.ifmo.se.application.mapper.RingMapper;
import ru.ifmo.se.application.repository.RingRepository;
import ru.ifmo.se.application.usecase.NotificationBroadcastUseCase;
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
    private NotificationBroadcastUseCase notificationBroadcastUseCase;

    @Override
    public RingResult create(RingCreateCommand command) {
        Ring ring = ringMapper.toEntity(command);
        Ring saved = ringRepository.save(ring);
        notificationBroadcastUseCase.broadcastChange(ActionType.CREATE, EntityType.RING, saved.getId());
        return ringMapper.toResult(saved);
    }

    @Override
    public RingResult getById(int id) {
        Ring ring = ringRepository.findById(id)
                .orElseThrow(() -> new RingNotFoundException(id));
        return ringMapper.toResult(ring);
    }

    @Override
    public RingResult update(int id, RingUpdateCommand command) {
        Ring ring = ringRepository.findById(id)
                .orElseThrow(() -> new RingNotFoundException(id));
        ringMapper.updateEntity(ring, command);
        Ring saved = ringRepository.save(ring);
        notificationBroadcastUseCase.broadcastChange(ActionType.UPDATE, EntityType.RING, saved.getId());
        return ringMapper.toResult(saved);
    }

    @Override
    public void delete(int id) {
        if (ringRepository.findById(id).isEmpty()) {
            throw new RingNotFoundException(id);
        }
        ringRepository.deleteById(id);
        notificationBroadcastUseCase.broadcastChange(ActionType.DELETE, EntityType.RING, id);
    }

    @Override
    public List<RingResult> getAll() {
        return ringRepository.findAll().stream()
                .map(ringMapper::toResult)
                .toList();
    }
}
