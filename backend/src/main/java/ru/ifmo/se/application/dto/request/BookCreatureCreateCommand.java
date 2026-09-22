package ru.ifmo.se.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.persistence.entity.BookCreatureType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreatureCreateCommand {
    @NotBlank
    private String name;

    @NotNull
    @Valid
    private CoordinatesRequest coordinates;

    @Positive
    private int age;

    @NotNull
    private BookCreatureType creatureType;

    @Positive
    private Integer creatureLocationId;

    @Positive
    private float attackLevel;

    @Positive
    private float defenseLevel;

    private Integer ringId;
}
