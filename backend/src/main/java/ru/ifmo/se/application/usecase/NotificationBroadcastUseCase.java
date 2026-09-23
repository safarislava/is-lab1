package ru.ifmo.se.application.usecase;

import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.application.enums.EntityType;

public interface NotificationBroadcastUseCase {
    void broadcastChange(ActionType action, EntityType entityType, int entityId);
}
