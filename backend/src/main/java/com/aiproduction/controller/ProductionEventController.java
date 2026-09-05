package com.aiproduction.controller;

import com.aiproduction.model.ProductionEvent;
import com.aiproduction.repository.ClickHouseProductionEventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
public class ProductionEventController {

    private final ClickHouseProductionEventRepository repository;

    public ProductionEventController(ClickHouseProductionEventRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody ProductionEvent event) {
        repository.save(event);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getEvents() {
        return ResponseEntity.ok(repository.findAll());
    }
}
