package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.dto.command.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.BookCreatureUpdateCommand;
import ru.ifmo.se.application.dto.query.CreatureSearchQuery;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.dto.result.PageResult;

public interface BookCreatureUseCase {
    BookCreatureResult create(BookCreatureCreateCommand command);

    BookCreatureResult getById(int id);

    BookCreatureResult update(int id, BookCreatureUpdateCommand command);

    void delete(int id);

    PageResult<BookCreatureResult> search(CreatureSearchQuery query);
}
