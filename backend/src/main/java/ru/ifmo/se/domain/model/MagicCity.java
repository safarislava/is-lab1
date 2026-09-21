package ru.ifmo.se.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MagicCity {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int area; //Значение поля должно быть больше 0
    private Long population; //Значение поля должно быть больше 0, Поле не может быть null
    private LocalDateTime establishmentDate;
    private BookCreatureType governor; //Поле может быть null
    private Boolean capital; //Поле может быть null
    private Float populationDensity; //Значение поля должно быть больше 0
}

