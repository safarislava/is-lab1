package ru.ifmo.se.application.dto.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportEvent {
    public enum Phase {
        START,
        COMPLETE
    }

    private int operationId;
    private String username;
    private Integer addedObjectsCount;
    private Phase phase;

    public static ImportEvent start(String username) {
        ImportEvent event = new ImportEvent();
        event.username = username;
        event.phase = Phase.START;
        return event;
    }

    public static ImportEvent complete(int operationId, int addedObjectsCount) {
        ImportEvent event = new ImportEvent();
        event.operationId = operationId;
        event.addedObjectsCount = addedObjectsCount;
        event.phase = Phase.COMPLETE;
        return event;
    }
}
