package com.aiproduction.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.Gemini;
import com.google.adk.models.VertexCredentials;
import com.google.adk.runner.InMemoryRunner;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.adk.sessions.SessionKey;
import com.google.adk.tools.mcp.McpToolset;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(McpToolset.class)
public class ProductionIntelligenceAgent {

    private static final String MODEL = "gemini-2.5-flash";
    private static final String APP_NAME = "ai-production-intelligence";
    private static final String USER_ID = "production-intelligence";

    private final InMemoryRunner runner;

    public ProductionIntelligenceAgent(McpToolset clickHouseMcpToolset) {
        String project = System.getenv("GOOGLE_CLOUD_PROJECT");
        String location = System.getenv().getOrDefault("GOOGLE_CLOUD_LOCATION", "us-central1");

        if (project == null || project.isBlank()) {
            throw new IllegalStateException(
                    "GOOGLE_CLOUD_PROJECT environment variable is required"
            );
        }

        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.getApplicationDefault();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Unable to obtain Google Cloud application credentials", e);
        }

        VertexCredentials vertexCredentials = VertexCredentials.builder()
                .project(project)
                .location(location)
                .credentials(credentials)
                .build();

        Gemini gemini = Gemini.builder()
                .modelName(MODEL)
                .vertexCredentials(vertexCredentials)
                .build();

        LlmAgent agent = LlmAgent.builder()
                .name("production_intelligence_agent")
                .description(
                        "AI agent for audiovisual production intelligence using "
                                + "Gemini and ClickHouse MCP tools."
                )
                .model(gemini)
                .instruction("""
                        You are an AI production intelligence agent for audiovisual production.

                        Use the available ClickHouse MCP tools to retrieve the production
                        data needed to answer the request.

                        Analyze production events with priority on:
                        - continuity of production
                        - schedule impact
                        - actor availability
                        - equipment availability
                        - weather constraints
                        - practical next actions

                        Base your conclusions on the available production data.
                        Do not invent events or facts that are not present in the data.
                        Provide a concise operational recommendation and the main reason.
                        """)
                .tools(clickHouseMcpToolset)
                .build();

        this.runner = new InMemoryRunner(agent);
    }

    public String analyze(String prompt) {
        String sessionId = "session-" + System.currentTimeMillis();

        SessionKey sessionKey = runner.sessionService()
                .createSession(
                        runner.appName(),
                        USER_ID,
                        null,
                        sessionId
                )
                .blockingGet()
                .sessionKey();

        Content promptContent = Content.fromParts(
                Part.fromText(prompt)
        );

        final String[] result = {null};

        runner.runAsync(sessionKey, promptContent)
                .blockingForEach(event -> {
                    if (event.finalResponse()) {
                        result[0] = event.stringifyContent();
                    }
                });

        if (result[0] == null || result[0].isBlank()) {
            throw new IllegalStateException(
                    "ADK agent completed without a final response"
            );
        }

        return result[0];
    }
}
