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
public class PageResponse {
    private List<BookCreatureResponse> content;
    private long total;
    private int page;
    private int size;
}
