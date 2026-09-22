package ru.ifmo.se.application.service;

import jakarta.inject.Named;
import jakarta.validation.Valid;
import ru.ifmo.se.application.dto.request.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.request.BookCreatureUpdateCommand;
import ru.ifmo.se.application.dto.request.CreatureSearchQuery;
import ru.ifmo.se.application.dto.response.BookCreatureResponse;
import ru.ifmo.se.application.dto.response.PageResponse;
import ru.ifmo.se.application.usecase.BookCreatureUseCase;

@Named
public class BookCreatureService implements BookCreatureUseCase {
    @Override
    public BookCreatureResponse create(@Valid BookCreatureCreateCommand command) {
        return null;
    }

    @Override
    public BookCreatureResponse getById(int id) {
        return null;
    }

    @Override
    public BookCreatureResponse update(int id, @Valid BookCreatureUpdateCommand command) {
        return null;
    }

    @Override
    public void delete(int id) {}

    @Override
    public PageResponse<BookCreatureResponse> search(@Valid CreatureSearchQuery query) {
        return null;
    }
}
