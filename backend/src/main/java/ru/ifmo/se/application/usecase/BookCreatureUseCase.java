package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.request.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.request.BookCreatureUpdateCommand;
import ru.ifmo.se.application.dto.request.CreatureSearchQuery;
import ru.ifmo.se.application.dto.response.BookCreatureResponse;
import ru.ifmo.se.application.dto.response.PageResponse;

public interface BookCreatureUseCase {
    BookCreatureResponse create(BookCreatureCreateCommand command);

    BookCreatureResponse getById(int id);

    BookCreatureResponse update(int id, BookCreatureUpdateCommand command);

    void delete(int id);

    PageResponse<BookCreatureResponse> search(CreatureSearchQuery query);
}
