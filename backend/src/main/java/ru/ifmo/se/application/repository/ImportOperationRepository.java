package ru.ifmo.se.application.repository;

import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.persistence.entity.ImportOperation;

import java.util.List;
import java.util.Optional;

public interface ImportOperationRepository {
    ImportOperation save(ImportOperation operation);

    Optional<ImportOperation> findById(int id);

    List<ImportOperation> findAll();

    List<ImportOperation> findAllByUsername(String username);

    PageResult<ImportOperation> findPage(int page, int size);

    PageResult<ImportOperation> findPageByUsername(String username, int page, int size);
}
