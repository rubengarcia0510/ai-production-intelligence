package com.aiproduction.analysis;

import com.aiproduction.ai.GeminiClient;
import com.aiproduction.repository.ClickHouseProductionEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductionAnalysisController.class)
@Import(ProductionAnalysisService.class)
class ProductionAnalysisControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ClickHouseProductionEventRepository repository;

    @MockBean
    GeminiClient geminiClient;

    @Test
    void shouldReturnProductionAnalysis() throws Exception {

        when(repository.findAll()).thenReturn(List.of(
                Map.of(
                        "event_type", "ACTOR_LATE",
                        "production_id", "DEMO-001",
                        "scene_id", "SCENE-12",
                        "description", "Actor delayed by 30 minutes"
                )
        ));

        when(geminiClient.analyze(anyString()))
                .thenReturn("Actor delay may affect Scene 12. Consider rescheduling.");

        mockMvc.perform(get("/api/v1/analysis")
                        .param("productionId", "DEMO-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionId").value("DEMO-001"))
                .andExpect(jsonPath("$.summary")
                        .value("Actor delay may affect Scene 12. Consider rescheduling."));
    }
}
