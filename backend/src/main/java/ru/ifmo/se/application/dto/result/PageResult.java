package ru.ifmo.se.application.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private List<T> content;
    private long total;
    private int page;
    private int size;
}
