package ru.ifmo.se.presentation.rest.sync;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionScoped;
import ru.ifmo.se.application.dto.event.ImportNotificationEvent;
import ru.ifmo.se.presentation.rest.NotificationBroadcastService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TransactionScoped
public class TransactionalImportBuffer {
    @Inject
    private NotificationBroadcastService broadcastService;

    private final Map<Integer, ImportNotificationEvent> events = new ConcurrentHashMap<>();

    public void onEvent(@Observes ImportNotificationEvent event) {
        events.put(event.result().getId(), event);
    }

    @PreDestroy
    public void onTransactionCompletion() {
        for (ImportNotificationEvent event : events.values()) {
            broadcastService.broadcastImportOperation(event.targetUsername(), event.result());
        }
    }
}
