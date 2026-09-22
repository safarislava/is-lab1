package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.request.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.request.BookCreatureUpdateCommand;
import ru.ifmo.se.application.dto.response.BookCreatureResponse;
import ru.ifmo.se.persistence.entity.BookCreature;
import ru.ifmo.se.persistence.entity.MagicCity;
import ru.ifmo.se.persistence.entity.Ring;

import java.time.ZonedDateTime;

@ApplicationScoped
public class BookCreatureMapper {
    @Inject
    private CoordinatesMapper coordinatesMapper;

    @Inject
    private MagicCityMapper magicCityMapper;

    @Inject
    private RingMapper ringMapper;

    public BookCreature toEntity(BookCreatureCreateCommand command, MagicCity city, Ring ring) {
        BookCreature creature = new BookCreature();
        creature.setName(command.getName());
        creature.setCoordinates(coordinatesMapper.toEntity(command.getCoordinates()));
        creature.setCreationDate(ZonedDateTime.now());
        creature.setAge(command.getAge());
        creature.setCreatureType(command.getCreatureType());
        creature.setCreatureLocation(city);
        creature.setAttackLevel(command.getAttackLevel());
        creature.setDefenseLevel(command.getDefenseLevel());
        creature.setRing(ring);
        return creature;
    }

    public void updateEntity(BookCreature creature, BookCreatureUpdateCommand command, MagicCity city, Ring ring) {
        creature.setName(command.getName());
        creature.setCoordinates(coordinatesMapper.toEntity(command.getCoordinates()));
        creature.setAge(command.getAge());
        creature.setCreatureType(command.getCreatureType());
        creature.setCreatureLocation(city);
        creature.setAttackLevel(command.getAttackLevel());
        creature.setDefenseLevel(command.getDefenseLevel());
        creature.setRing(ring);
    }

    public BookCreatureResponse toResponse(BookCreature creature) {
        if (creature == null) {
            return null;
        }
        return new BookCreatureResponse(
                creature.getId(),
                creature.getName(),
                coordinatesMapper.toResponse(creature.getCoordinates()),
                creature.getCreationDate(),
                creature.getAge(),
                creature.getCreatureType(),
                magicCityMapper.toResponse(creature.getCreatureLocation()),
                creature.getAttackLevel(),
                creature.getDefenseLevel(),
                ringMapper.toResponse(creature.getRing())
        );
    }
}
