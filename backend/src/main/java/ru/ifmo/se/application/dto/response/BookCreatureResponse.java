package ru.ifmo.se.application.dto.response;

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
public class BookCreatureResponse {
    private int id;
    private String name;
    private CoordinatesResponse coordinates;
    private ZonedDateTime creationDate;
    private int age;
    private BookCreatureType creatureType;
    private MagicCityResponse creatureLocation;
    private float attackLevel;
    private float defenseLevel;
    private RingResponse ring;
}
