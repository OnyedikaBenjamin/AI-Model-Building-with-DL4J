package com.DL4J.player_performance_ai.service;

import com.DL4J.player_performance_ai.ai.PlayerAIModel;
import com.DL4J.player_performance_ai.dto.PlayerPerformanceDto;
import com.DL4J.player_performance_ai.model.PlayerPerformance;
import com.DL4J.player_performance_ai.repository.PlayerPerformanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlayerPerformanceService {

    private final PlayerPerformanceRepository repository;
    private final PlayerAIModel playerAIModel;
    private static final String MODEL_PATH = "src/main/resources/player_model.zip";

    @Autowired
    public PlayerPerformanceService(PlayerPerformanceRepository repository,PlayerAIModel playerAIModel) {
        this.repository = repository;
        this.playerAIModel = playerAIModel;
    }

    public List<PlayerPerformanceDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public PlayerPerformanceDto add(PlayerPerformanceDto dto) {
        PlayerPerformance performance = repository.save(toEntity(dto));
        playerAIModel.trainModel();  // Train the model after adding new data
        return toDto(performance);
    }

    /**
     * Update an existing player performance by ID.
     * @param id The ID of the player performance to update.
     * @param dto The updated player performance data.
     * @return The updated PlayerPerformanceDto or null if the ID was not found.
     */
    public PlayerPerformanceDto update(Long id, PlayerPerformanceDto dto) {
        Optional<PlayerPerformance> existingPerformance = repository.findById(id);
        if (existingPerformance.isPresent()) {
            PlayerPerformance performance = existingPerformance.get();
            performance.setAverage(dto.getAverage());
            performance.setStrikeRate(dto.getStrikeRate());
            performance.setBowlingAverage(dto.getBowlingAverage());
            performance.setEconomyRate(dto.getEconomyRate());
            performance.setFieldingStats(dto.getFieldingStats());
            performance.setLabel(dto.getLabel());
            PlayerPerformance updatedPerformance = repository.save(performance);
            playerAIModel.trainModel();  // Train the model after adding new data
            return toDto(updatedPerformance);
        }
        return null;
    }

    /**
     * Delete a player performance by ID.
     * @param id The ID of the player performance to delete.
     * @return true if the performance was deleted, false otherwise.
     */
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            playerAIModel.trainModel();  // Train the model after adding new data
            return true;
        }
        return false;
    }

    /**
     * Predicts if the player is suitable based on provided metrics.
     * @param features An array containing player metrics.
     * @return true if the player is suitable, false otherwise.
     */
    public boolean predictPlayerSuitability(float[] features) {
        return playerAIModel.predict(features);
    }

    private PlayerPerformance toEntity(PlayerPerformanceDto dto) {
        PlayerPerformance entity = new PlayerPerformance();
        entity.setAverage(dto.getAverage());
        entity.setStrikeRate(dto.getStrikeRate());
        entity.setBowlingAverage(dto.getBowlingAverage());
        entity.setEconomyRate(dto.getEconomyRate());
        entity.setFieldingStats(dto.getFieldingStats());
        entity.setLabel(dto.getLabel());
        return entity;
    }

    private PlayerPerformanceDto toDto(PlayerPerformance entity) {
        PlayerPerformanceDto dto = new PlayerPerformanceDto();
        dto.setId(entity.getId());
        dto.setAverage(entity.getAverage());
        dto.setStrikeRate(entity.getStrikeRate());
        dto.setBowlingAverage(entity.getBowlingAverage());
        dto.setEconomyRate(entity.getEconomyRate());
        dto.setFieldingStats(entity.getFieldingStats());
        dto.setLabel(entity.getLabel());
        return dto;
    }

    public void trainModel() {
        playerAIModel.trainModel();
    }
}

