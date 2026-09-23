package ru.ifmo.se.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MagicCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "TEXT CHECK (TRIM(name) != '')")
    private String name;

    @Column(nullable = false, columnDefinition = "INT CHECK (area > 0)")
    private int area;

    @Column(nullable = false, columnDefinition = "BIGINT CHECK (population > 0)")
    private Long population;

    private LocalDateTime establishmentDate;

    @Enumerated(EnumType.STRING)
    private BookCreatureType governor;

    private Boolean capital;

    @Column(columnDefinition = "DECIMAL CHECK (populationDensity > 0)")
    private Float populationDensity;
}

