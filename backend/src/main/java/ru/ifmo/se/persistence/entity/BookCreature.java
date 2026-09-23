package ru.ifmo.se.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import ru.ifmo.se.persistence.converter.ZonedDateTimeConverter;

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
    private int id;

    @Column(nullable = false, columnDefinition = "TEXT CHECK (trim(name) != '')")
    private String name;

    @Embedded
    private Coordinates coordinates;

    @Convert(converter = ZonedDateTimeConverter.class)
    @Column(nullable = false, columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    private ZonedDateTime creationDate;

    @Column(nullable = false, columnDefinition = "INT CHECK (age > 0)")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCreatureType creatureType;

    @ManyToOne
    private MagicCity creatureLocation;

    @Column(nullable = false, columnDefinition = "REAL CHECK (attackLevel > 0)")
    private float attackLevel;

    @Column(nullable = false, columnDefinition = "REAL CHECK (defenseLevel > 0)")
    private float defenseLevel;

    @OneToOne
    private Ring ring;
}
