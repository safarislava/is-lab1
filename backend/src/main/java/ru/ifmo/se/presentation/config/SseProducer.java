package ru.ifmo.se.presentation.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;

@ApplicationScoped
public class SseProducer {

    @Context
    private Sse sse;

    @Produces
    @ApplicationScoped
    public Sse produceSse() {
        return sse;
    }

    @Produces
    @ApplicationScoped
    public SseBroadcaster produceSseBroadcaster() {
        return sse.newBroadcaster();
    }
}
