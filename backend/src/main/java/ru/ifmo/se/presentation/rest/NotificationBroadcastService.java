package ru.ifmo.se.presentation.rest;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;
import ru.ifmo.se.application.dto.event.NotificationEvent;
import ru.ifmo.se.application.dto.result.ImportOperationResult;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class NotificationBroadcastService {
    @Inject
    private Sse sse;

    private SseBroadcaster broadcaster;
    private final List<SseSession> sessions = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        this.broadcaster = sse.newBroadcaster();
        this.broadcaster.onClose(sink -> sessions.removeIf(s -> s.sink().equals(sink)));
        this.broadcaster.onError((sink, throwable) -> sessions.removeIf(s -> s.sink().equals(sink)));
    }

    public void registerSink(SseEventSink sink, String username, boolean isAdmin) {
        if (!sink.isClosed()) {
            broadcaster.register(sink);
            sessions.add(new SseSession(sink, username, isAdmin));
        }
    }

    public void broadcastEvent(NotificationEvent event) {
        OutboundSseEvent sseEvent = sse.newEventBuilder()
                .name("entity-change")
                .data(NotificationEvent.class, event)
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .build();
        broadcaster.broadcast(sseEvent);
    }

    public void broadcastImportOperation(String targetUsername, ImportOperationResult result) {
        OutboundSseEvent sseEvent = sse.newEventBuilder()
                .name("import-operation")
                .data(ImportOperationResult.class, result)
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .build();

        sessions.removeIf(s -> s.sink().isClosed());

        for (SseSession session : sessions) {
            boolean isTargetUser = targetUsername.equals(session.username());
            boolean isAdmin = session.admin();

            if (isTargetUser || isAdmin) {
                session.sink().send(sseEvent);
            }
        }
    }

    private record SseSession(SseEventSink sink, String username, boolean admin) { }
}
