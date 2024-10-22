package com.DL4J.player_performance_ai.controller;

import com.DL4J.player_performance_ai.dto.PlayerPerformanceDto;
import com.DL4J.player_performance_ai.service.PlayerPerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/performance")
public class PlayerPerformanceController {

    private final PlayerPerformanceService service;

    @GetMapping
    public List<PlayerPerformanceDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    public PlayerPerformanceDto add(@RequestBody PlayerPerformanceDto dto) {
        return service.add(dto);
    }

    /**
     * Endpoint to predict if a player is suitable based on provided metrics.
     * Example JSON input:
     * {
     *   "average": 50.5,
     *   "strikeRate": 140.0,
     *   "bowlingAverage": 20.0,
     *   "economyRate": 4.2,
     *   "fieldingStats": 15
     * }
     */
    @PostMapping("/predict")
    public boolean predictPlayer(@RequestBody Map<String, Float> playerMetrics) {
        float[] features = new float[]{
                playerMetrics.get("average"),
                playerMetrics.get("strikeRate"),
                playerMetrics.get("bowlingAverage"),
                playerMetrics.get("economyRate"),
                playerMetrics.get("fieldingStats")
        };
        return service.predictPlayerSuitability(features);
    }
}
