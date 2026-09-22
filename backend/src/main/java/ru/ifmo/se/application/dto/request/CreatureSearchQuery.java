package ru.ifmo.se.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.domain.model.BookCreatureType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatureSearchQuery {
    private int page;
    private int size = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";

    private String nameFilter;
    private BookCreatureType creatureTypeFilter;
    private String cityNameFilter;
    private String ringNameFilter;
    private Integer minAge;
    private Integer maxAge;
    private Float minAttackLevel;
    private Float maxAttackLevel;
    private Float minDefenseLevel;
    private Float maxDefenseLevel;
}
