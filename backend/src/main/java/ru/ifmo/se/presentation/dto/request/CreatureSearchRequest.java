package ru.ifmo.se.presentation.dto.request;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.persistence.entity.BookCreatureType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatureSearchRequest {
    @QueryParam("page")
    @DefaultValue("0")
    private int page;

    @QueryParam("size")
    @DefaultValue("10")
    private int size;

    @QueryParam("sortBy")
    private String sortBy;

    @QueryParam("sortDirection")
    private String sortDirection;

    @QueryParam("nameFilter")
    private String nameFilter;

    @QueryParam("creatureTypeFilter")
    private BookCreatureType creatureTypeFilter;

    @QueryParam("cityNameFilter")
    private String cityNameFilter;

    @QueryParam("ringNameFilter")
    private String ringNameFilter;
}
