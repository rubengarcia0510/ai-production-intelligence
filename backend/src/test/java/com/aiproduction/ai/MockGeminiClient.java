package com.aiproduction.ai;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class MockGeminiClient implements GeminiClient {

    @Override
    public String analyze(String prompt) {
        return "Mock production analysis for testing.";
    }
}
