package ru.ifmo.se.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    @Column(columnDefinition = "BIGINT CHECK (x <= 500)")
    private long x; //Максимальное значение поля: 500

    @Column(nullable = false, columnDefinition = "DECIMAL CHECK (y > -780)")
    private Float y; //Значение поля должно быть больше -780, Поле не может быть null
}

