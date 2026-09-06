package com.aiproduction.ai;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class GeminiConfiguredCondition implements Condition {

    @Override
    public boolean matches(
            ConditionContext context,
            AnnotatedTypeMetadata metadata) {

        String apiKey = context.getEnvironment().getProperty("GOOGLE_API_KEY");

        return apiKey != null && !apiKey.isBlank();
    }
}
