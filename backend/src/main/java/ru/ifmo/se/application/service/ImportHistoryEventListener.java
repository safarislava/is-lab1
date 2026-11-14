package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.event.ImportEvent;
import ru.ifmo.se.application.dto.event.ImportNotificationEvent;
import ru.ifmo.se.application.mapper.ImportOperationMapper;
import ru.ifmo.se.application.repository.ImportOperationRepository;
import ru.ifmo.se.application.repository.UserRepository;
import ru.ifmo.se.persistence.entity.ImportOperation;
import ru.ifmo.se.persistence.entity.ImportStatus;
import ru.ifmo.se.persistence.entity.User;

import java.time.ZonedDateTime;

@ApplicationScoped
public class ImportHistoryEventListener {

    @Inject
    private ImportOperationRepository importOperationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Event<ImportNotificationEvent> notificationPublisher;

    @Inject
    private ImportOperationMapper mapper;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void onImportEvent(@Observes ImportEvent event) {
        if (event.getPhase() == ImportEvent.Phase.START) {
            handleStart(event);
        } else {
            handleComplete(event);
        }
    }

    private void handleStart(ImportEvent event) {
        User user = userRepository.findByUsername(event.getUsername()).orElseThrow();

        ImportOperation operation = new ImportOperation();
        operation.setUser(user);
        operation.setStatus(ImportStatus.FAILED);
        operation.setCreationDate(ZonedDateTime.now());

        ImportOperation saved = importOperationRepository.save(operation);
        event.setOperationId(saved.getId());

        notificationPublisher.fire(new ImportNotificationEvent(event.getUsername(), mapper.toResult(saved)));
    }

    private void handleComplete(ImportEvent event) {
        ImportOperation operation = importOperationRepository.findById(event.getOperationId()).orElseThrow();

        operation.setStatus(ImportStatus.SUCCESS);
        operation.setAddedObjectsCount(event.getAddedObjectsCount());
        ImportOperation saved = importOperationRepository.save(operation);

        notificationPublisher.fire(new ImportNotificationEvent(saved.getUser().getUsername(), mapper.toResult(saved)));
    }
}
