package ru.ifmo.se.persistence.entity;

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
    private long x;

    @Column(nullable = false, columnDefinition = "DECIMAL CHECK (y > -780)")
    private Float y;
}

