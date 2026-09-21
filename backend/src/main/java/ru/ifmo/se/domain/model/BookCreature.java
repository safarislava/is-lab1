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
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private ZonedDateTime creationDate; // Поле не может быть null, генерируется автоматически
    private int age; // Значение поля должно быть больше 0
    private BookCreatureType creatureType; // Поле не может быть null
    private MagicCity creatureLocation; // Поле может быть null
    private float attackLevel; // Значение поля должно быть больше 0
    private float defenseLevel; // Значение поля должно быть больше 0
    private Ring ring;
}

