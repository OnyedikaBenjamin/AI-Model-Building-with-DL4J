package com.DL4J.player_performance_ai.repository;

import com.DL4J.player_performance_ai.model.PlayerPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerPerformanceRepository extends JpaRepository<PlayerPerformance, Long> {
}
