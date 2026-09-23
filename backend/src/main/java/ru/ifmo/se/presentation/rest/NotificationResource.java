package ru.ifmo.se.presentation.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.SseEventSink;

@Path("/notifications")
@RequestScoped
public class NotificationResource {
    @Inject
    private NotificationBroadcastService notificationBroadcastService;

    @GET
    @Path("/events")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void events(@Context SseEventSink sink) {
        notificationBroadcastService.registerSink(sink);
    }
}
