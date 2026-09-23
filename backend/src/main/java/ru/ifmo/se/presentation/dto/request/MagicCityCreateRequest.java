package ru.ifmo.se.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class MagicCityCreateRequest {
    @NotBlank
    private String name;

    @Positive
    private int area;

    @NotNull
    @Positive
    private Long population;

    private LocalDateTime establishmentDate;

    private BookCreatureType governor;

    private Boolean capital;

    @Positive
    private Float populationDensity;
}
