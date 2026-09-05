package com.aiproduction.repository;

import java.util.List;
import java.util.Map;

public interface ProductionEventRepository {

    void save(Map<String, Object> event);

    List<Map<String, Object>> findAll();
}
