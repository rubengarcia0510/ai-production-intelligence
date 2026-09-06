package com.aiproduction.mcp;

import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.StreamableHttpServerParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.Duration;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "mcp.clickhouse.enabled", havingValue = "true")
public class McpClickHouseConfig {

    @Bean
    public McpToolset clickHouseMcpToolset() {
        String url = System.getenv("CLICKHOUSE_MCP_URL");
        String token = System.getenv("CLICKHOUSE_MCP_AUTH_TOKEN");

        if (url == null || url.isBlank() || token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "MCP ClickHouse is enabled but CLICKHOUSE_MCP_URL or CLICKHOUSE_MCP_AUTH_TOKEN is missing"
            );
        }

        StreamableHttpServerParameters parameters =
                StreamableHttpServerParameters.builder()
                        .url(url)
                        .headers(Map.of(
                                "Authorization",
                                "Bearer " + token
                        ))
                        .timeout(Duration.ofSeconds(10))
                        .readTimeout(Duration.ofSeconds(30))
                        .terminateOnClose(true)
                        .build();

        return new McpToolset(parameters);
    }
}
