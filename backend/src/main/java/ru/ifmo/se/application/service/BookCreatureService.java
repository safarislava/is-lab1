package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import ru.ifmo.se.application.dto.request.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.request.BookCreatureUpdateCommand;
import ru.ifmo.se.application.dto.request.CreatureSearchQuery;
import ru.ifmo.se.application.dto.response.BookCreatureNotFoundException;
import ru.ifmo.se.application.dto.response.BookCreatureResponse;
import ru.ifmo.se.application.dto.response.MagicCityNotFoundException;
import ru.ifmo.se.application.dto.response.PageResponse;
import ru.ifmo.se.application.dto.response.RingAlreadyInUseException;
import ru.ifmo.se.application.dto.response.RingNotFoundException;
import ru.ifmo.se.application.mapper.BookCreatureMapper;
import ru.ifmo.se.application.repository.BookCreatureRepository;
import ru.ifmo.se.application.repository.MagicCityRepository;
import ru.ifmo.se.application.repository.RingRepository;
import ru.ifmo.se.application.usecase.BookCreatureUseCase;
import ru.ifmo.se.persistence.entity.BookCreature;
import ru.ifmo.se.persistence.entity.MagicCity;
import ru.ifmo.se.persistence.entity.Ring;

import java.util.List;

@ApplicationScoped
public class BookCreatureService implements BookCreatureUseCase {
    @Inject
    private BookCreatureRepository bookCreatureRepository;

    @Inject
    private MagicCityRepository magicCityRepository;

    @Inject
    private RingRepository ringRepository;

    @Inject
    private BookCreatureMapper bookCreatureMapper;

    @Override
    public BookCreatureResponse create(@Valid BookCreatureCreateCommand command) {
        MagicCity city = resolveCity(command.getCreatureLocationId());
        Ring ring = resolveRing(command.getRingId(), null);
        BookCreature creature = bookCreatureMapper.toEntity(command, city, ring);
        BookCreature saved = bookCreatureRepository.save(creature);
        return bookCreatureMapper.toResponse(saved);
    }

    @Override
    public BookCreatureResponse getById(int id) {
        BookCreature creature = bookCreatureRepository.findById(id)
                .orElseThrow(() -> new BookCreatureNotFoundException(id));
        return bookCreatureMapper.toResponse(creature);
    }

    @Override
    public BookCreatureResponse update(int id, @Valid BookCreatureUpdateCommand command) {
        BookCreature creature = bookCreatureRepository.findById(id)
                .orElseThrow(() -> new BookCreatureNotFoundException(id));
        MagicCity city = resolveCity(command.getCreatureLocationId());
        Ring ring = resolveRing(command.getRingId(), id);
        bookCreatureMapper.updateEntity(creature, command, city, ring);
        BookCreature saved = bookCreatureRepository.save(creature);
        return bookCreatureMapper.toResponse(saved);
    }

    @Override
    public void delete(int id) {
        if (bookCreatureRepository.findById(id).isEmpty()) {
            throw new BookCreatureNotFoundException(id);
        }
        bookCreatureRepository.deleteById(id);
    }

    @Override
    public PageResponse<BookCreatureResponse> search(@Valid CreatureSearchQuery query) {
        PageResponse<BookCreature> page = bookCreatureRepository.findBySearchQuery(query);
        List<BookCreatureResponse> content = page.getContent().stream()
                .map(bookCreatureMapper::toResponse)
                .toList();
        return new PageResponse<>(
                content,
                page.getTotal(),
                page.getPage(),
                page.getSize()
        );
    }

    private MagicCity resolveCity(Integer cityId) {
        if (cityId == null) {
            return null;
        }
        return magicCityRepository.findById(cityId)
                .orElseThrow(() -> new MagicCityNotFoundException(cityId));
    }

    private Ring resolveRing(Integer ringId, Integer currentCreatureId) {
        if (ringId == null) {
            return null;
        }
        Ring ring = ringRepository.findById(ringId)
                .orElseThrow(() -> new RingNotFoundException(ringId));
        bookCreatureRepository.findByRingId(ringId)
                .ifPresent(another -> {
                    if (currentCreatureId == null || another.getId() != currentCreatureId) {
                        throw new RingAlreadyInUseException(another);
                    }
                });
        return ring;
    }
}
