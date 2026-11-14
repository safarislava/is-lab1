package ru.ifmo.se.presentation.rest.sync;

import ru.ifmo.se.application.enums.EntityType;

import java.io.Serializable;

public record EntityKey(EntityType entityType, int entityId) implements Serializable { }
