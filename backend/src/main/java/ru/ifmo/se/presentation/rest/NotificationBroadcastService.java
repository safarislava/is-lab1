package ru.ifmo.se.presentation.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;
import ru.ifmo.se.application.usecase.NotificationBroadcastUseCase;
import ru.ifmo.se.presentation.dto.response.NotificationEvent;

import java.time.ZonedDateTime;

@ApplicationScoped
public class NotificationBroadcastService implements NotificationBroadcastUseCase {

    @Inject
    private SseBroadcaster broadcaster;

    @Inject
    private Sse sse;

    public void registerSink(SseEventSink sink) {
        if (sink == null) {
            return;
        }
        broadcaster.register(sink);
    }

    @Override
    public void broadcastChange(String action, String entityName, int entityId) {
        NotificationEvent event = new NotificationEvent(entityName, action, entityId, ZonedDateTime.now());
        OutboundSseEvent sseEvent = sse.newEventBuilder()
                .name("entity-change")
                .data(NotificationEvent.class, event)
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .build();
        broadcaster.broadcast(sseEvent);
    }
}
