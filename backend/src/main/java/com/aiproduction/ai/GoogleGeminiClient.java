package com.aiproduction.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;


@Component
@Profile("!test")
@Conditional(GeminiConfiguredCondition.class)
public class GoogleGeminiClient implements GeminiClient {

    private static final String MODEL = "gemini-3.6-flash";

    private final Client client;

    public GoogleGeminiClient() {
        String apiKey = System.getenv("GOOGLE_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GOOGLE_API_KEY environment variable is required");
        }

        this.client = Client.builder()
                .apiKey(apiKey)
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
