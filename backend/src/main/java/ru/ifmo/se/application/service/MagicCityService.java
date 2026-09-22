package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import ru.ifmo.se.application.dto.request.MagicCityCreateCommand;
import ru.ifmo.se.application.dto.request.MagicCityUpdateCommand;
import ru.ifmo.se.application.dto.response.MagicCityNotFoundException;
import ru.ifmo.se.application.dto.response.MagicCityResponse;
import ru.ifmo.se.application.dto.response.MagicCitySwapSameException;
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

    @Override
    public MagicCityResponse create(@Valid MagicCityCreateCommand command) {
        MagicCity city = magicCityMapper.toEntity(command);
        MagicCity saved = magicCityRepository.save(city);
        return magicCityMapper.toResponse(saved);
    }

    @Override
    public MagicCityResponse getById(int id) {
        MagicCity city = magicCityRepository.findById(id)
                .orElseThrow(() -> new MagicCityNotFoundException(id));
        return magicCityMapper.toResponse(city);
    }

    @Override
    public MagicCityResponse update(int id, @Valid MagicCityUpdateCommand command) {
        MagicCity city = magicCityRepository.findById(id)
                .orElseThrow(() -> new MagicCityNotFoundException(id));
        magicCityMapper.updateEntity(city, command);
        MagicCity saved = magicCityRepository.save(city);
        return magicCityMapper.toResponse(saved);
    }

    @Override
    public void delete(int id, Integer replacementCityId) {
        if (magicCityRepository.findById(id).isEmpty()) {
            throw new MagicCityNotFoundException(id);
        }
        if (replacementCityId.equals(id)) {
            throw new MagicCitySwapSameException(id);
        }
        MagicCity replacementCity = magicCityRepository.findById(replacementCityId)
            .orElseThrow(() -> new MagicCityNotFoundException(replacementCityId));
        bookCreatureRepository.reassignCity(id, replacementCity);
        magicCityRepository.deleteById(id);
    }

    @Override
    public List<MagicCityResponse> getAll() {
        return magicCityRepository.findAll().stream()
                .map(magicCityMapper::toResponse)
                .toList();
    }
}
