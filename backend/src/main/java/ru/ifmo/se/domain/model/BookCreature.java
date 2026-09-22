package ru.ifmo.se.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreature {
    private int id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private int age;
    private BookCreatureType creatureType;
    private MagicCity creatureLocation;
    private float attackLevel;
    private float defenseLevel;
    private Ring ring;
}
