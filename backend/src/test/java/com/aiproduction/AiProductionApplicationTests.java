package com.aiproduction;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "clickhouse.startup-check.enabled=false"
    }
)
class AiProductionApplicationTests {

    @MockBean
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }
}
