package ru.ifmo.se.presentation.rest.sync;

import ru.ifmo.se.application.enums.EntityType;

public record EntityKey(EntityType entityType, int entityId) { }
