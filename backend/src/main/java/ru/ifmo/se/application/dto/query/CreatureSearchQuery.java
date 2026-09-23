package ru.ifmo.se.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.persistence.entity.BookCreatureType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatureSearchQuery {
    private int page;
    private int size;
    private String sortBy;
    private String sortDirection;

    private String nameFilter;
    private BookCreatureType creatureTypeFilter;
    private String cityNameFilter;
    private String ringNameFilter;
}
