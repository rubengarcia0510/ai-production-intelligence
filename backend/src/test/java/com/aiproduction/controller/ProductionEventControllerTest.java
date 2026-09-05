package com.aiproduction.controller;

import com.aiproduction.model.ProductionEvent;
import com.aiproduction.repository.ClickHouseProductionEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductionEventControllerTest {

    @Test
    void shouldCreateEvent() {
        ClickHouseProductionEventRepository repository =
            mock(ClickHouseProductionEventRepository.class);

        ProductionEventController controller =
            new ProductionEventController(repository);

        ProductionEvent event = new ProductionEvent(
            "ACTOR_LATE",
            "DEMO-001",
            "SCENE-12",
            "ACTOR-07",
            "Studio A",
            "Actor delayed by 30 minutes",
            Instant.parse("2026-09-05T12:00:00Z")
        );

        ResponseEntity<Void> response = controller.createEvent(event);

        assertEquals(200, response.getStatusCode().value());
        verify(repository).save(event);
    }

    @Test
    void shouldReturnEvents() {
        ClickHouseProductionEventRepository repository =
            mock(ClickHouseProductionEventRepository.class);

        List<Map<String, Object>> events = List.of(
            Map.of(
                "event_type", "ACTOR_LATE",
                "production_id", "DEMO-001"
            )
        );

        when(repository.findAll()).thenReturn(events);

        ProductionEventController controller =
            new ProductionEventController(repository);

        ResponseEntity<List<Map<String, Object>>> response =
            controller.getEvents();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(events, response.getBody());
        verify(repository).findAll();
    }
}
