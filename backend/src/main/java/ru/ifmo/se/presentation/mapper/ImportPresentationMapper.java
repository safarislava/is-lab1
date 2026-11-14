package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.application.dto.result.ImportOperationResult;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.presentation.dto.response.ImportOperationPageResponse;
import ru.ifmo.se.presentation.dto.response.ImportOperationResponse;

import java.util.List;

@ApplicationScoped
public class ImportPresentationMapper {

    public ImportOperationResponse toResponse(ImportOperationResult result) {
        if (result == null) {
            return null;
        }
        return new ImportOperationResponse(
                result.getId(),
                result.getStatus() != null ? result.getStatus().name() : null,
                result.getUsername(),
                result.getAddedObjectsCount(),
                result.getCreationDate()
        );
    }

    public ImportOperationPageResponse toPageResponse(PageResult<ImportOperationResult> pageResult) {
        if (pageResult == null) {
            return null;
        }
        List<ImportOperationResponse> content = pageResult.getContent() != null
                ? pageResult.getContent().stream().map(this::toResponse).toList()
                : List.of();
        return new ImportOperationPageResponse(
                content,
                pageResult.getTotal(),
                pageResult.getPage(),
                pageResult.getSize()
        );
    }
}
