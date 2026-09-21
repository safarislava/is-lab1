package ru.ifmo.se.infrastructure.persistence.entity;

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
    @Column(nullable = false, columnDefinition = "INT CHECK (id > 0)")
    private int id;

    @Column(nullable = false, columnDefinition = "TEXT CHECK (TRIM(name) != '')")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Column(nullable = false, columnDefinition = "INT CHECK (area > 0)")
    private int area; //Значение поля должно быть больше 0

    @Column(nullable = false, columnDefinition = "BIGINT CHECK (population > 0)")
    private Long population; //Значение поля должно быть больше 0, Поле не может быть null

    private LocalDateTime establishmentDate;

    @Enumerated(EnumType.STRING)
    private BookCreatureType governor; //Поле может быть null

    private Boolean capital; //Поле может быть null

    @Column(columnDefinition = "DECIMAL CHECK (populationDensity > 0)")
    private Float populationDensity; //Значение поля должно быть больше 0
}

