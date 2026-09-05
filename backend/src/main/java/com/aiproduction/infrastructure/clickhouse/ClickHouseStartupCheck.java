package com.aiproduction.infrastructure.clickhouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    name = "clickhouse.startup-check.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class ClickHouseStartupCheck implements ApplicationRunner {

    private static final Logger log =
        LoggerFactory.getLogger(ClickHouseStartupCheck.class);

    private final JdbcTemplate jdbcTemplate;

    public ClickHouseStartupCheck(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            if (Integer.valueOf(1).equals(result)) {
                log.info("ClickHouse connection check: UP");
            } else {
                log.warn("ClickHouse connection check: unexpected result {}", result);
            }
        } catch (Exception e) {
            log.error("ClickHouse connection check: DOWN", e);
        }
    }
}
