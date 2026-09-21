package ru.ifmo.se.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ring {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Integer power; //Значение поля должно быть больше 0, Поле не может быть null
}

