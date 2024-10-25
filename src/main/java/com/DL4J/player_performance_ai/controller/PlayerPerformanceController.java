package com.DL4J.player_performance_ai.controller;

import com.DL4J.player_performance_ai.dto.PlayerPerformanceDto;
import com.DL4J.player_performance_ai.service.PlayerPerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Update an existing player performance by ID
    @PutMapping("/{id}")
    public ResponseEntity<PlayerPerformanceDto> update(
            @PathVariable Long id, @RequestBody PlayerPerformanceDto dto) {
        PlayerPerformanceDto updatedDto = service.update(id, dto);
        return updatedDto != null ?
                ResponseEntity.ok(updatedDto) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Delete a player performance by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean isDeleted = service.delete(id);
        return isDeleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

    /**
     * Endpoint to trigger model training with the latest data.
     * This will retrain the AI model using all player data in the database.
     */
    @PostMapping("/train")
    public ResponseEntity<String> trainModel() {
        try {
            service.trainModel();  // Call the service to train the model
            return ResponseEntity.ok("Model training started successfully.");
        } catch (Exception e) {
            log.error("Error during model training", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to train the model.");
        }
    }
}
