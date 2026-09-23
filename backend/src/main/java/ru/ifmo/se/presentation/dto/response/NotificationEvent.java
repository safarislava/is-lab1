package ru.ifmo.se.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.application.enums.ActionType;
import ru.ifmo.se.application.enums.EntityType;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private ActionType action;
    private EntityType entityType;
    private int entityId;
    private ZonedDateTime timestamp;
}
