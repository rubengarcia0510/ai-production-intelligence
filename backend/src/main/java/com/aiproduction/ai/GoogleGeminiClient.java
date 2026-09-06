package com.aiproduction.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Conditional(GeminiConfiguredCondition.class)
public class GoogleGeminiClient implements GeminiClient {

    private static final String MODEL = "gemini-2.5-flash";

    private final Client client;

    public GoogleGeminiClient() {
        String project = System.getenv("GOOGLE_CLOUD_PROJECT");
        String location = System.getenv().getOrDefault("GOOGLE_CLOUD_LOCATION", "us-central1");

        if (project == null || project.isBlank()) {
            throw new IllegalStateException(
                    "GOOGLE_CLOUD_PROJECT environment variable is required");
        }

        this.client = Client.builder()
                .project(project)
                .location(location)
                .vertexAI(true)
                .build();
    }

    @Override
    public String analyze(String prompt) {
        GenerateContentResponse response = client.models.generateContent(
                MODEL,
                prompt,
                null
        );

        return response.text();
    }
}
