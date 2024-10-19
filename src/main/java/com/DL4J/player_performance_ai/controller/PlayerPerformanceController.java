package com.DL4J.player_performance_ai.controller;

import com.DL4J.player_performance_ai.dto.PlayerPerformanceDto;
import com.DL4J.player_performance_ai.service.PlayerPerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
