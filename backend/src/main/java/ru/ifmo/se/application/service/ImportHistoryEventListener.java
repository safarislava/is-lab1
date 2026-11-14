package ru.ifmo.se.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.application.dto.event.ImportEvent;
import ru.ifmo.se.application.mapper.ImportOperationMapper;
import ru.ifmo.se.application.repository.ImportOperationRepository;
import ru.ifmo.se.application.repository.UserRepository;
import ru.ifmo.se.persistence.entity.ImportOperation;
import ru.ifmo.se.persistence.entity.ImportStatus;
import ru.ifmo.se.persistence.entity.User;
import ru.ifmo.se.presentation.rest.NotificationBroadcastService;

import java.time.ZonedDateTime;

@ApplicationScoped
public class ImportHistoryEventListener {

    @Inject
    private ImportOperationRepository importOperationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private NotificationBroadcastService notificationBroadcastService;

    @Inject
    private ImportOperationMapper mapper;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void onImportEvent(@Observes ImportEvent event) {
        if (event == null) {
            return;
        }

        if (event.getPhase() == ImportEvent.Phase.START) {
            handleStart(event);
        } else {
            handleComplete(event);
        }
    }

    private void handleStart(ImportEvent event) {
        if (event.getUsername() == null) {
            return;
        }

        User user = userRepository.findByUsername(event.getUsername()).orElse(null);
        if (user == null) {
            return;
        }

        ImportOperation operation = new ImportOperation();
        operation.setUser(user);
        operation.setStatus(ImportStatus.FAILED);
        operation.setCreationDate(ZonedDateTime.now());

        ImportOperation saved = importOperationRepository.save(operation);
        event.setOperationId(saved.getId());

        notificationBroadcastService.broadcastImportOperation(event.getUsername(), mapper.toResult(saved));
    }

    private void handleComplete(ImportEvent event) {
        if (event.getOperationId() == 0) {
            return;
        }

        ImportOperation operation = importOperationRepository.findById(event.getOperationId()).orElse(null);
        if (operation == null) {
            return;
        }

        operation.setStatus(ImportStatus.SUCCESS);
        operation.setAddedObjectsCount(event.getAddedObjectsCount());
        ImportOperation saved = importOperationRepository.save(operation);

        String username = saved.getUser().getUsername();
        notificationBroadcastService.broadcastImportOperation(username, mapper.toResult(saved));
    }
}
