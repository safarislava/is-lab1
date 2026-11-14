package ru.ifmo.se.application.dto.event;

import ru.ifmo.se.application.dto.result.ImportOperationResult;

import java.io.Serializable;

public record ImportNotificationEvent(String targetUsername, ImportOperationResult result) implements Serializable { }
