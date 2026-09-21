package ru.ifmo.se.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "TEXT CHECK (trim(name) != '')")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Column(nullable = false, columnDefinition = "INT CHECK (power > 0)")
    private Integer power; //Значение поля должно быть больше 0, Поле не может быть null
}

