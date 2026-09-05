package com.aiproduction.repository;

import com.aiproduction.model.ProductionEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class ClickHouseProductionEventRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClickHouseProductionEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ProductionEvent event) {
        jdbcTemplate.update(
            """
            INSERT INTO production_events
            (event_type, production_id, scene_id, actor_id, location, description, timestamp)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
            event.eventType(),
            event.productionId(),
            event.sceneId(),
            event.actorId(),
            event.location(),
            event.description(),
            Timestamp.from(event.timestamp())
        );
    }

    public List<Map<String, Object>> findAll() {
        return jdbcTemplate.queryForList(
            """
            SELECT
                event_type,
                production_id,
                scene_id,
                actor_id,
                location,
                description,
                timestamp
            FROM production_events
            ORDER BY timestamp DESC
            """
        );
    }
}
