package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import ru.ifmo.se.application.dto.event.ImportEvent;
import ru.ifmo.se.application.dto.event.ImportNotificationEvent;
import ru.ifmo.se.application.mapper.ImportOperationMapper;
import ru.ifmo.se.persistence.entity.ImportOperation;

@ApplicationScoped
public class ImportHistoryEventListener {

    @Inject
    private ImportHistoryService importHistoryService;

    @Inject
    private Event<ImportNotificationEvent> notificationPublisher;

    @Inject
    private ImportOperationMapper mapper;

    public void onImportEvent(@Observes ImportEvent event) {
        if (event.getPhase() == ImportEvent.Phase.START) {
            ImportOperation saved = importHistoryService.createFailedOperation(event.getUsername());
            event.setOperationId(saved.getId());
            event.setCreationDate(saved.getCreationDate());
            notificationPublisher.fire(new ImportNotificationEvent(event.getUsername(), mapper.toResult(saved)));
        } else {
            ImportOperation saved = importHistoryService.completeOperation(event.getOperationId(), event.getAddedObjectsCount());
            notificationPublisher.fire(new ImportNotificationEvent(saved.getUser().getUsername(), mapper.toResult(saved)));
        }
    }
}
