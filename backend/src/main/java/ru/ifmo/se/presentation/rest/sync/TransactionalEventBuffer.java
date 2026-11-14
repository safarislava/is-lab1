package ru.ifmo.se.presentation.rest.sync;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.TransactionSynchronizationRegistry;
import ru.ifmo.se.application.dto.event.NotificationEvent;
import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.presentation.rest.NotificationBroadcastService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TransactionScoped
public class TransactionalEventBuffer {
    @Resource
    private TransactionSynchronizationRegistry registry;

    @Inject
    private NotificationBroadcastService broadcastService;

    private final Map<EntityKey, NotificationEvent> events = new ConcurrentHashMap<>();

    public void onEvent(@Observes NotificationEvent event) {
        EntityKey globalKey = new EntityKey(event.getEntityType(), 0);

        if (event.getEntityId() == 0) {
            handleGlobalEvent(globalKey, event);
            return;
        }

        NotificationEvent existingGlobal = events.get(globalKey);
        if (existingGlobal != null) {
            existingGlobal.setTimestamp(event.getTimestamp());
            return;
        }

        EntityKey key = new EntityKey(event.getEntityType(), event.getEntityId());
        events.compute(key, (k, existing) -> mergeEvents(existing, event));
    }

    @PreDestroy
    public void onTransactionCompletion() {
        if (registry.getTransactionStatus() == Status.STATUS_COMMITTED) {
            for (NotificationEvent event : events.values()) {
                broadcastService.broadcastEvent(event);
            }
        }
    }

    private void handleGlobalEvent(EntityKey globalKey, NotificationEvent globalEvent) {
        events.keySet().removeIf(key -> key.entityType() == globalEvent.getEntityType());
        events.put(globalKey, globalEvent);
    }

    private NotificationEvent mergeEvents(NotificationEvent existing, NotificationEvent incoming) {
        if (existing == null) {
            return new NotificationEvent(
                    incoming.getAction(),
                    incoming.getEntityType(),
                    incoming.getEntityId(),
                    incoming.getTimestamp()
            );
        }

        ActionType prev = existing.getAction();
        ActionType next = incoming.getAction();

        if (prev == ActionType.CREATE && next == ActionType.DELETE) {
            return null;
        }

        if (prev == ActionType.UPDATE && next == ActionType.DELETE) {
            existing.setAction(ActionType.DELETE);
        } else if (prev == ActionType.DELETE && next == ActionType.CREATE) {
            existing.setAction(ActionType.UPDATE);
        }

        existing.setTimestamp(incoming.getTimestamp());
        return existing;
    }
}
