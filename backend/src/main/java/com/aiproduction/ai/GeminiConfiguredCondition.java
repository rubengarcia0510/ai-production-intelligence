package com.aiproduction.ai;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class GeminiConfiguredCondition implements Condition {

    @Override
    public boolean matches(
            ConditionContext context,
            AnnotatedTypeMetadata metadata) {

        String project = context.getEnvironment().getProperty("GOOGLE_CLOUD_PROJECT");

        return project != null && !project.isBlank();
    }
}
