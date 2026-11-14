package ru.ifmo.se.application.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.persistence.entity.ImportStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportOperationResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private ImportStatus status;
    private String username;
    private Integer addedObjectsCount;
    private ZonedDateTime creationDate;
}
