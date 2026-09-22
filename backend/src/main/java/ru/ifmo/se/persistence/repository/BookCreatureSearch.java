package ru.ifmo.se.persistence.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ru.ifmo.se.application.dto.query.CreatureSearchQuery;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.exception.UnknownSearchSortByException;
import ru.ifmo.se.persistence.entity.BookCreature;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookCreatureSearch {
    private static final Map<String, String> SORT_PATHS = Map.of(
            "name", "b.name",
            "age", "b.age",
            "creation_date", "b.creationDate",
            "creature_type", "b.creatureType",
            "city_name", "c.name",
            "ring_name", "r.name",
            "coordinate_x", "b.coordinates.x",
            "coordinate_y", "b.coordinates.y",
            "attack_level", "b.attackLevel",
            "defense_level", "b.defenseLevel"
    );

    private final EntityManager entityManager;
    private final CreatureSearchQuery query;

    public BookCreatureSearch(EntityManager entityManager, CreatureSearchQuery query) {
        this.entityManager = entityManager;
        this.query = query;
    }

    public PageResult<BookCreature> execute() {
        int page = query.getPage();
        int size = query.getSize();

        List<String> conditions = new ArrayList<>();
        Map<String, Object> params = new ConcurrentHashMap<>();
        buildConditions(conditions, params);

        String where = conditions.isEmpty() ? "" : String.format("WHERE %s", String.join(" AND ", conditions));
        String from = String.format("FROM BookCreature b LEFT JOIN b.creatureLocation c LEFT JOIN b.ring r %s", where);

        long total = countTotal(from, params);
        if (total == 0) {
            return new PageResult<>(List.of(), 0, page, size);
        }

        List<BookCreature> content = getPage(from, params, page, size);
        return new PageResult<>(content, total, page, size);
    }

    private long countTotal(String from, Map<String, Object> params) {
        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(b) " + from, Long.class);
        params.forEach(countQuery::setParameter);
        return countQuery.getSingleResult();
    }

    private List<BookCreature> getPage(String from, Map<String, Object> params, int page, int size) {
        String sort = buildSort();
        TypedQuery<BookCreature> dataQuery = entityManager.createQuery(
                String.format("SELECT b %s %s", from, sort), BookCreature.class)
                .setFirstResult(page * size)
                .setMaxResults(size);
        params.forEach(dataQuery::setParameter);
        return dataQuery.getResultList();
    }

    private void buildConditions(List<String> conditions, Map<String, Object> params) {
        if (query.getNameFilter() != null) {
            conditions.add("LOWER(b.name) LIKE CONCAT('%', :name, '%')");
            params.put("name", query.getNameFilter().toLowerCase(Locale.ROOT));
        }
        if (query.getCreatureTypeFilter() != null) {
            conditions.add("b.creatureType = CONCAT('%', :type, '%')");
            params.put("type", query.getCreatureTypeFilter());
        }
        if (query.getCityNameFilter() != null) {
            conditions.add("LOWER(c.name) LIKE CONCAT('%', :city, '%')");
            params.put("city", query.getCityNameFilter().toLowerCase(Locale.ROOT));
        }
        if (query.getRingNameFilter() != null) {
            conditions.add("LOWER(r.name) LIKE CONCAT('%', :ring, '%')");
            params.put("ring", query.getRingNameFilter().toLowerCase(Locale.ROOT));
        }
    }

    private String buildSort() {
        if (query.getSortBy() == null) {
            return "";
        }
        String sortPath = resolveSortPath(query.getSortBy());
        String sortDirection = "DESC".equalsIgnoreCase(query.getSortDirection()) ? "DESC" : "ASC";
        return String.format("ORDER BY %s %s", sortPath, sortDirection);
    }

    private String resolveSortPath(String sortBy) {
        String path = SORT_PATHS.get(sortBy);
        if (path == null) {
            throw new UnknownSearchSortByException(sortBy);
        }
        return path;
    }
}
