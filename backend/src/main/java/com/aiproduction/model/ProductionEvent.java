package com.aiproduction.model;

import java.time.Instant;

public record ProductionEvent(
    String eventType,
    String productionId,
    String sceneId,
    String actorId,
    String location,
    String description,
    Instant timestamp
) {
}
