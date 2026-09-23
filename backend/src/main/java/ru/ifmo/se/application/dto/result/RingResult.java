package ru.ifmo.se.application.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RingResult {
    private int id;
    private String name;
    private Integer power;
}
