package com.aiproduction.analysis;

import com.aiproduction.ai.GeminiClient;
import com.aiproduction.repository.ClickHouseProductionEventRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductionAnalysisServiceTest {

    @Test
    void shouldAnalyzeProductionEvents() {
        ClickHouseProductionEventRepository repository =
                mock(ClickHouseProductionEventRepository.class);

        GeminiClient geminiClient = mock(GeminiClient.class);

        when(repository.findAll()).thenReturn(List.of(
                Map.of(
                        "event_type", "ACTOR_LATE",
                        "production_id", "DEMO-001",
                        "scene_id", "SCENE-12",
                        "description", "Actor delayed by 30 minutes"
                ),
                Map.of(
                        "event_type", "CAMERA_FAILURE",
                        "production_id", "DEMO-001",
                        "scene_id", "SCENE-12",
                        "description", "Camera failed during production"
                )
        ));

        when(geminiClient.analyze(anyString()))
                .thenReturn("Camera failure and actor delay threaten Scene 12. Reorder production activities.");

        ProductionAnalysisService service =
                new ProductionAnalysisService(repository, geminiClient, Optional.empty());

        ProductionAnalysis result = service.analyze("DEMO-001");

        assertEquals("DEMO-001", result.productionId());
        assertEquals(
                "Camera failure and actor delay threaten Scene 12. Reorder production activities.",
                result.summary()
        );

        verify(geminiClient).analyze(anyString());
    }

    @Test
    void shouldReturnFallbackWhenProductionHasNoEvents() {
        ClickHouseProductionEventRepository repository =
                mock(ClickHouseProductionEventRepository.class);

        GeminiClient geminiClient = mock(GeminiClient.class);

        when(repository.findAll()).thenReturn(List.of());

        ProductionAnalysisService service =
                new ProductionAnalysisService(repository, geminiClient, Optional.empty());

        ProductionAnalysis result = service.analyze("UNKNOWN");

        assertEquals("UNKNOWN", result.productionId());
        assertEquals(
                "No production events were found.",
                result.summary()
        );

        verifyNoInteractions(geminiClient);
    }
}
