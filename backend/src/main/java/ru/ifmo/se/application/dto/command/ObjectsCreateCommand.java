package ru.ifmo.se.application.dto.command;

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
public class ObjectsCreateCommand {
    @NotNull
    private List<@Valid @NotNull BatchBookCreatureCreateCommand> creatures = new ArrayList<>();

    @NotNull
    private List<@Valid @NotNull BatchRingCreateCommand> rings = new ArrayList<>();

    @NotNull
    private List<@Valid @NotNull BatchMagicCityCreateCommand> cities = new ArrayList<>();
}
