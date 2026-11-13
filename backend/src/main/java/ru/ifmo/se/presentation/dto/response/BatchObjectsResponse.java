package ru.ifmo.se.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchObjectsResponse {
    private List<MagicCityResponse> cities;
    private List<RingResponse> rings;
    private List<BookCreatureResponse> creatures;
}
