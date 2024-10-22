package com.DL4J.player_performance_ai.service;

import com.DL4J.player_performance_ai.ai.PlayerAIModel;
import com.DL4J.player_performance_ai.dto.PlayerPerformanceDto;
import com.DL4J.player_performance_ai.model.PlayerPerformance;
import com.DL4J.player_performance_ai.repository.PlayerPerformanceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class PlayerPerformanceService {

    private final PlayerPerformanceRepository repository;
    private PlayerAIModel playerAIModel;

    public List<PlayerPerformanceDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public PlayerPerformanceDto add(PlayerPerformanceDto dto) {
        PlayerPerformance performance = repository.save(toEntity(dto));
        return toDto(performance);
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
}

