package com.aiproduction.analysis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analysis")
public class ProductionAnalysisController {

    private final ProductionAnalysisService analysisService;

    public ProductionAnalysisController(
            ProductionAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping
    public ResponseEntity<ProductionAnalysis> analyze(
            @RequestParam String productionId) {

        return ResponseEntity.ok(
                analysisService.analyze(productionId)
        );
    }
}
