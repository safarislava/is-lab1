package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import ru.ifmo.se.application.dto.request.RingCreateCommand;
import ru.ifmo.se.application.dto.request.RingUpdateCommand;
import ru.ifmo.se.application.dto.response.RingNotFoundException;
import ru.ifmo.se.application.dto.response.RingResponse;
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

    @Override
    public RingResponse create(@Valid RingCreateCommand command) {
        Ring ring = ringMapper.toEntity(command);
        Ring saved = ringRepository.save(ring);
        return ringMapper.toResponse(saved);
    }

    @Override
    public RingResponse getById(int id) {
        Ring ring = ringRepository.findById(id)
                .orElseThrow(() -> new RingNotFoundException(id));
        return ringMapper.toResponse(ring);
    }

    @Override
    public RingResponse update(int id, @Valid RingUpdateCommand command) {
        Ring ring = ringRepository.findById(id)
                .orElseThrow(() -> new RingNotFoundException(id));
        ringMapper.updateEntity(ring, command);
        Ring saved = ringRepository.save(ring);
        return ringMapper.toResponse(saved);
    }

    @Override
    public void delete(int id) {
        if (ringRepository.findById(id).isEmpty()) {
            throw new RingNotFoundException(id);
        }
        ringRepository.deleteById(id);
    }

    @Override
    public List<RingResponse> getAll() {
        return ringRepository.findAll().stream()
                .map(ringMapper::toResponse)
                .toList();
    }
}
