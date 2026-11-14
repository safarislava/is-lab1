package ru.ifmo.se.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportOperationResponse {
    private int id;
    private String status;
    private String username;
    private Integer addedObjectsCount;
    private ZonedDateTime creationDate;
}
