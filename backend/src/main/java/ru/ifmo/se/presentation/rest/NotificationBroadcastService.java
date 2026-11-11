package ru.ifmo.se.presentation.rest;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;
import ru.ifmo.se.application.dto.event.NotificationEvent;

@ApplicationScoped
public class NotificationBroadcastService {

    @Inject
    private Sse sse;

    private SseBroadcaster broadcaster;

    @PostConstruct
    public void init() {
        this.broadcaster = sse.newBroadcaster();
    }

    public void registerSink(SseEventSink sink) {
        if (sink == null) {
            return;
        }
        broadcaster.register(sink);
    }

    public void onEntityChange(@Observes(during = TransactionPhase.AFTER_SUCCESS) NotificationEvent event) {
        OutboundSseEvent sseEvent = sse.newEventBuilder()
                .name("entity-change")
                .data(NotificationEvent.class, event)
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .build();
        broadcaster.broadcast(sseEvent);
    }
}
