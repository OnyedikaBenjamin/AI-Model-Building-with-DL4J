package com.DL4J.player_performance_ai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double average;
    private double strikeRate;
    private double bowlingAverage;
    private double economyRate;
    private int fieldingStats;
    private int label;  // 1 for good, 0 for bad
}
