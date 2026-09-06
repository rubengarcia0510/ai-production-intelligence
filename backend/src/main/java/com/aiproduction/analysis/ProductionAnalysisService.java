package com.aiproduction.analysis;

import com.aiproduction.ai.GeminiClient;
import com.aiproduction.repository.ClickHouseProductionEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductionAnalysisService {

    private static final Logger log =
            LoggerFactory.getLogger(ProductionAnalysisService.class);

    private final ClickHouseProductionEventRepository repository;
    private final GeminiClient geminiClient;

    public ProductionAnalysisService(
            ClickHouseProductionEventRepository repository,
            GeminiClient geminiClient) {
        this.repository = repository;
        this.geminiClient = geminiClient;
    }

    public ProductionAnalysis analyze(String productionId) {
        long totalStart = System.currentTimeMillis();

        long clickHouseStart = System.currentTimeMillis();

        List<Map<String, Object>> events = repository.findAll().stream()
                .filter(event -> productionId.equals(event.get("production_id")))
                .toList();

        long clickHouseDuration = System.currentTimeMillis() - clickHouseStart;

        log.info("Production analysis ClickHouse query took {} ms, productionId={}, events={}",
                clickHouseDuration, productionId, events.size());

        if (events.isEmpty()) {
            log.info("Production analysis finished without events in {} ms, productionId={}",
                    System.currentTimeMillis() - totalStart, productionId);

            return new ProductionAnalysis(
                    productionId,
                    "No production events were found.",
                    "No action is recommended until production data is available."
            );
        }

        String prompt = buildPrompt(productionId, events);

        long geminiStart = System.currentTimeMillis();

        String analysis = geminiClient.analyze(prompt);

        long geminiDuration = System.currentTimeMillis() - geminiStart;

        log.info("Production analysis Gemini call took {} ms, productionId={}",
                geminiDuration, productionId);

        log.info("Production analysis total took {} ms, productionId={}",
                System.currentTimeMillis() - totalStart, productionId);

        return new ProductionAnalysis(
                productionId,
                analysis,
                "Follow the recommendation generated from the current production context."
        );
    }

    private String buildPrompt(
            String productionId,
            List<Map<String, Object>> events) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are an AI production intelligence assistant for audiovisual production.

                Analyze the following production events and provide:
                1. A concise summary of the current production situation.
                2. The most appropriate operational recommendation.
                3. The main reason for that recommendation.

                Prioritize continuity of production, schedule impact, actor availability,
                equipment availability, weather constraints, and practical next actions.

                Production ID: %s

                Events:
                """.formatted(productionId));

        events.forEach(event ->
                prompt.append("\n- ")
                        .append(event)
                        .append("\n")
        );

        return prompt.toString();
    }
}
