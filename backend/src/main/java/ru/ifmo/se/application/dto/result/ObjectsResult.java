package ru.ifmo.se.application.dto.result;

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
public class ObjectsResult {
    private List<MagicCityResult> cities = new ArrayList<>();
    private List<RingResult> rings = new ArrayList<>();
    private List<BookCreatureResult> creatures = new ArrayList<>();
}
