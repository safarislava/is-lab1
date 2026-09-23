package ru.ifmo.se.application.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.persistence.entity.BookCreatureType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MagicCityResult {
    private int id;
    private String name;
    private int area;
    private Long population;
    private LocalDateTime establishmentDate;
    private BookCreatureType governor;
    private Boolean capital;
    private Float populationDensity;
}
