package com.aiproduction.mcp;

import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.StreamableHttpServerParameters;
import java.time.Duration;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class McpToolsetCompileTest {

    @Test
    void shouldCreateMcpToolsetConfiguration() {
        StreamableHttpServerParameters parameters =
                StreamableHttpServerParameters.builder()
                        .url("http://localhost:8000/mcp")
                        .headers(Map.of(
                                "Authorization",
                                "Bearer test-token"
                        ))
                        .timeout(Duration.ofSeconds(10))
                        .readTimeout(Duration.ofSeconds(30))
                        .terminateOnClose(true)
                        .build();

        McpToolset toolset = new McpToolset(parameters);

        assertNotNull(toolset);

        toolset.close();
    }
}
