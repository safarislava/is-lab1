package ru.ifmo.se.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.command.BookCreatureCreateCommand;
import ru.ifmo.se.application.dto.command.BookCreatureUpdateCommand;
import ru.ifmo.se.application.dto.query.CreatureSearchQuery;
import ru.ifmo.se.application.dto.result.BookCreatureResult;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.presentation.dto.request.BookCreatureCreateRequest;
import ru.ifmo.se.presentation.dto.request.BookCreatureUpdateRequest;
import ru.ifmo.se.presentation.dto.request.CreatureSearchRequest;
import ru.ifmo.se.presentation.dto.response.BookCreatureResponse;
import ru.ifmo.se.presentation.dto.response.PageResponse;

import java.util.List;

@ApplicationScoped
public class BookCreaturePresentationMapper {
    @Inject
    private CoordinatesPresentationMapper coordinatesMapper;

    @Inject
    private MagicCityPresentationMapper magicCityMapper;

    @Inject
    private RingPresentationMapper ringMapper;

    public BookCreatureCreateCommand toCommand(BookCreatureCreateRequest request) {
        if (request == null) {
            return null;
        }
        return new BookCreatureCreateCommand(
                request.getName(),
                coordinatesMapper.toCommand(request.getCoordinates()),
                request.getAge(),
                request.getCreatureType(),
                request.getCreatureLocationId(),
                request.getAttackLevel(),
                request.getDefenseLevel(),
                request.getRingId()
        );
    }

    public BookCreatureUpdateCommand toCommand(BookCreatureUpdateRequest request) {
        if (request == null) {
            return null;
        }
        return new BookCreatureUpdateCommand(
                request.getName(),
                coordinatesMapper.toCommand(request.getCoordinates()),
                request.getAge(),
                request.getCreatureType(),
                request.getCreatureLocationId(),
                request.getAttackLevel(),
                request.getDefenseLevel(),
                request.getRingId()
        );
    }

    public CreatureSearchQuery toQuery(CreatureSearchRequest request) {
        if (request == null) {
            return new CreatureSearchQuery();
        }
        return new CreatureSearchQuery(
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDirection(),
                request.getNameFilter(),
                request.getCreatureTypeFilter(),
                request.getCityNameFilter(),
                request.getRingNameFilter()
        );
    }

    public BookCreatureResponse toResponse(BookCreatureResult result) {
        if (result == null) {
            return null;
        }
        return new BookCreatureResponse(
                result.getId(),
                result.getName(),
                coordinatesMapper.toResponse(result.getCoordinates()),
                result.getCreationDate(),
                result.getAge(),
                result.getCreatureType(),
                magicCityMapper.toResponse(result.getCreatureLocation()),
                result.getAttackLevel(),
                result.getDefenseLevel(),
                ringMapper.toResponse(result.getRing())
        );
    }

    public PageResponse toResponse(PageResult<BookCreatureResult> pageResult) {
        if (pageResult == null) {
            return null;
        }
        List<BookCreatureResponse> content = pageResult.getContent().stream()
                .map(this::toResponse)
                .toList();
        return new PageResponse(
                content,
                pageResult.getTotal(),
                pageResult.getPage(),
                pageResult.getSize()
        );
    }
}
