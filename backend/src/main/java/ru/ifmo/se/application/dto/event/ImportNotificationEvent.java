package ru.ifmo.se.application.dto.event;

import ru.ifmo.se.application.dto.result.ImportOperationResult;

public record ImportNotificationEvent(String targetUsername, ImportOperationResult result) { }
