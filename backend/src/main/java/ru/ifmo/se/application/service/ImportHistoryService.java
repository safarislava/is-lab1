package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.result.ImportOperationResult;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.mapper.ImportOperationMapper;
import ru.ifmo.se.application.repository.ImportOperationRepository;
import ru.ifmo.se.application.usecase.ImportHistoryUseCase;
import ru.ifmo.se.persistence.entity.ImportOperation;

import java.util.List;

@ApplicationScoped
public class ImportHistoryService implements ImportHistoryUseCase {

    @Inject
    private ImportOperationRepository importOperationRepository;

    @Inject
    private ImportOperationMapper mapper;

    @Override
    public List<ImportOperationResult> getHistory(String username, boolean isAdmin) {
        List<ImportOperation> operations = isAdmin
                ? importOperationRepository.findAll()
                : importOperationRepository.findAllByUsername(username);

        return operations.stream()
                .map(mapper::toResult)
                .toList();
    }

    @Override
    public PageResult<ImportOperationResult> getHistory(String username, boolean isAdmin, int page, int size) {
        PageResult<ImportOperation> pageResult = isAdmin
                ? importOperationRepository.findPage(page, size)
                : importOperationRepository.findPageByUsername(username, page, size);

        List<ImportOperationResult> results = pageResult.getContent().stream()
                .map(mapper::toResult)
                .toList();

        return new PageResult<>(results, pageResult.getTotal(), pageResult.getPage(), pageResult.getSize());
    }
}
