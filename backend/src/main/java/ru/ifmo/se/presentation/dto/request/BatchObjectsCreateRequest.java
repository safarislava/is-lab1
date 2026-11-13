package ru.ifmo.se.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchObjectsCreateRequest {
    @NotNull
    private List<@Valid @NotNull BatchBookCreatureCreateRequest> creatures = new ArrayList<>();

    @NotNull
    private List<@Valid @NotNull BatchRingCreateRequest> rings = new ArrayList<>();

    @NotNull
    private List<@Valid @NotNull BatchMagicCityCreateRequest> cities = new ArrayList<>();
}
