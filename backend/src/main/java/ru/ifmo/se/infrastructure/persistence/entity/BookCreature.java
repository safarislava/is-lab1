package ru.ifmo.se.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false, columnDefinition = "TEXT CHECK (trim(name) != '')")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Embedded
    private Coordinates coordinates; //Поле не может быть null

    @Column(nullable = false, columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false, columnDefinition = "INT CHECK (age > 0)")
    private int age; //Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCreatureType creatureType; //Поле не может быть null

    @ManyToOne(fetch = FetchType.LAZY)
    private MagicCity creatureLocation; //Поле может быть null

    @Column(nullable = false, columnDefinition = "REAL CHECK (attackLevel > 0)")
    private float attackLevel; //Значение поля должно быть больше 0

    @Column(nullable = false, columnDefinition = "REAL CHECK (defenseLevel > 0)")
    private float defenseLevel; //Значение поля должно быть больше 0

    @OneToOne(fetch = FetchType.LAZY)
    private Ring ring;
}
