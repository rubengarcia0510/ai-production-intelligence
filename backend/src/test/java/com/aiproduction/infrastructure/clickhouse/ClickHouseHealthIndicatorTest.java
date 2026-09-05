package com.aiproduction.infrastructure.clickhouse;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClickHouseHealthIndicatorTest {

    @Test
    void shouldReportUpWhenClickHouseReturnsOne() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class))
            .thenReturn(1);

        ClickHouseHealthIndicator indicator =
            new ClickHouseHealthIndicator(jdbcTemplate);

        assertEquals("UP", indicator.health().getStatus().getCode());
    }

    @Test
    void shouldReportDownWhenClickHouseQueryFails() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class))
            .thenThrow(new RuntimeException("connection failed"));

        ClickHouseHealthIndicator indicator =
            new ClickHouseHealthIndicator(jdbcTemplate);

        assertEquals("DOWN", indicator.health().getStatus().getCode());
    }
}
