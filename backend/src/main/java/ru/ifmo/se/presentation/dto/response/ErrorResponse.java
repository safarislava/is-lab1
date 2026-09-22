package ru.ifmo.se.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private ZonedDateTime timestamp;
    private List<String> details;

    public ErrorResponse(String message, int status) {
        this(message, status, ZonedDateTime.now(), List.of());
    }
}
