package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import ru.ifmo.se.application.dto.command.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.BookCreatureUpdateCommand;
import ru.ifmo.se.application.dto.query.CreatureSearchQuery;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.exception.BookCreatureNotFoundException;
import ru.ifmo.se.application.exception.MagicCityNotFoundException;
import ru.ifmo.se.application.exception.RingAlreadyInUseException;
import ru.ifmo.se.application.exception.RingNotFoundException;
import ru.ifmo.se.application.mapper.BookCreatureMapper;
import ru.ifmo.se.application.repository.BookCreatureRepository;
import ru.ifmo.se.application.repository.MagicCityRepository;
import ru.ifmo.se.application.repository.RingRepository;
import ru.ifmo.se.application.usecase.BookCreatureUseCase;
import ru.ifmo.se.application.usecase.NotificationBroadcastUseCase;
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

    @Inject
    private NotificationBroadcastUseCase notificationBroadcastUseCase;

    @Override
    public BookCreatureResult create(@Valid BookCreatureCreateCommand command) {
        MagicCity city = resolveCity(command.getCreatureLocationId());
        Ring ring = resolveRing(command.getRingId(), null);
        BookCreature creature = bookCreatureMapper.toEntity(command, city, ring);
        BookCreature saved = bookCreatureRepository.save(creature);
        notificationBroadcastUseCase.broadcastChange("CREATE", "BOOK_CREATURE", saved.getId());
        return bookCreatureMapper.toResult(saved);
    }

    @Override
    public BookCreatureResult getById(int id) {
        BookCreature creature = bookCreatureRepository.findById(id)
                .orElseThrow(() -> new BookCreatureNotFoundException(id));
        return bookCreatureMapper.toResult(creature);
    }

    @Override
    public BookCreatureResult update(int id, @Valid BookCreatureUpdateCommand command) {
        BookCreature creature = bookCreatureRepository.findById(id)
                .orElseThrow(() -> new BookCreatureNotFoundException(id));
        MagicCity city = resolveCity(command.getCreatureLocationId());
        Ring ring = resolveRing(command.getRingId(), id);
        bookCreatureMapper.updateEntity(creature, command, city, ring);
        BookCreature saved = bookCreatureRepository.save(creature);
        notificationBroadcastUseCase.broadcastChange("UPDATE", "BOOK_CREATURE", saved.getId());
        return bookCreatureMapper.toResult(saved);
    }

    @Override
    public void delete(int id) {
        if (bookCreatureRepository.findById(id).isEmpty()) {
            throw new BookCreatureNotFoundException(id);
        }
        bookCreatureRepository.deleteById(id);
        notificationBroadcastUseCase.broadcastChange("DELETE", "BOOK_CREATURE", id);
    }

    @Override
    public PageResult<BookCreatureResult> search(@Valid CreatureSearchQuery query) {
        PageResult<BookCreature> page = bookCreatureRepository.findBySearchQuery(query);
        List<BookCreatureResult> content = page.getContent().stream()
                .map(bookCreatureMapper::toResult)
                .toList();
        return new PageResult<>(
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
