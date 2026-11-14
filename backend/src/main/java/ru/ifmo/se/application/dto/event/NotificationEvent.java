package ru.ifmo.se.application.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.application.enums.EntityType;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private ActionType action;
    private EntityType entityType;
    private int entityId;
    private ZonedDateTime timestamp;

    public NotificationEvent(ActionType action, EntityType entityType, int entityId) {
        this(action, entityType, entityId, ZonedDateTime.now());
    }
}
