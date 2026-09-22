package ru.ifmo.se.application.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.persistence.entity.BookCreatureType;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreatureResult {
    private int id;
    private String name;
    private CoordinatesResult coordinates;
    private ZonedDateTime creationDate;
    private int age;
    private BookCreatureType creatureType;
    private MagicCityResult creatureLocation;
    private float attackLevel;
    private float defenseLevel;
    private RingResult ring;
}
