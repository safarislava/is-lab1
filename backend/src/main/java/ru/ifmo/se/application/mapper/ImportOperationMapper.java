package ru.ifmo.se.application.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.result.ImportOperationResult;
import ru.ifmo.se.persistence.entity.ImportOperation;

@ApplicationScoped
public class ImportOperationMapper {

    public ImportOperationResult toResult(ImportOperation operation) {
        if (operation == null) {
            return null;
        }
        return new ImportOperationResult(
                operation.getId(),
                operation.getStatus(),
                operation.getUser() != null ? operation.getUser().getUsername() : null,
                operation.getAddedObjectsCount(),
                operation.getCreationDate()
        );
    }
}
