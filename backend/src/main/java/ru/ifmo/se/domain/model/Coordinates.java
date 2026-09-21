package ru.ifmo.se.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    private long x; //Максимальное значение поля: 500
    private Float y; //Значение поля должно быть больше -780, Поле не может быть null
}

