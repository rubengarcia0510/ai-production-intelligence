package com.aiproduction.infrastructure.clickhouse;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("clickhouse")
public class ClickHouseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public ClickHouseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            if (Integer.valueOf(1).equals(result)) {
                return Health.up()
                    .withDetail("database", "ClickHouse")
                    .withDetail("query", "SELECT 1")
                    .build();
            }

            return Health.down()
                .withDetail("database", "ClickHouse")
                .withDetail("reason", "Unexpected SELECT 1 result")
                .build();

        } catch (Exception e) {
            return Health.down(e)
                .withDetail("database", "ClickHouse")
                .build();
        }
    }
}
