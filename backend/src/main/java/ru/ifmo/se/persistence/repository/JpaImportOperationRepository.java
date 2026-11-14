package ru.ifmo.se.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.ifmo.se.application.dto.result.PageResult;
import ru.ifmo.se.application.repository.ImportOperationRepository;
import ru.ifmo.se.persistence.entity.ImportOperation;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaImportOperationRepository implements ImportOperationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ImportOperation save(ImportOperation operation) {
        if (operation.getId() == 0) {
            entityManager.persist(operation);
            entityManager.flush();
            return operation;
        }
        ImportOperation merged = entityManager.merge(operation);
        entityManager.flush();
        return merged;
    }

    @Override
    public Optional<ImportOperation> findById(int id) {
        return Optional.ofNullable(entityManager.find(ImportOperation.class, id));
    }

    @Override
    public List<ImportOperation> findAll() {
        return entityManager.createQuery(
                "SELECT io FROM ImportOperation io ORDER BY io.creationDate DESC, io.id DESC",
                ImportOperation.class
        ).getResultList();
    }

    @Override
    public List<ImportOperation> findAllByUsername(String username) {
        return entityManager.createQuery(
                "SELECT io FROM ImportOperation io WHERE io.user.username = :username "
                        + "ORDER BY io.creationDate DESC, io.id DESC",
                ImportOperation.class
        )
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public PageResult<ImportOperation> findPage(int page, int size) {
        Long total = entityManager.createQuery(
                "SELECT COUNT(io) FROM ImportOperation io", Long.class)
                .getSingleResult();
        List<ImportOperation> content = entityManager.createQuery(
                "SELECT io FROM ImportOperation io ORDER BY io.creationDate DESC, io.id DESC",
                ImportOperation.class
        )
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
        return new PageResult<>(content, total != null ? total : 0, page, size);
    }

    @Override
    public PageResult<ImportOperation> findPageByUsername(String username, int page, int size) {
        Long total = entityManager.createQuery(
                "SELECT COUNT(io) FROM ImportOperation io WHERE io.user.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        List<ImportOperation> content = entityManager.createQuery(
                "SELECT io FROM ImportOperation io WHERE io.user.username = :username "
                        + "ORDER BY io.creationDate DESC, io.id DESC",
                ImportOperation.class
        )
                .setParameter("username", username)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
        return new PageResult<>(content, total != null ? total : 0, page, size);
    }
}
