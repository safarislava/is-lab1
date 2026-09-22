package ru.ifmo.se.application.usecase;

public interface NotificationBroadcastUseCase {
    void broadcastChange(String action, String entityName, int entityId);
}
