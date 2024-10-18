package com.DL4J.player_performance_ai.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPerformanceDto implements Serializable {

    private Long id;
    private double average;
    private double strikeRate;
    private double bowlingAverage;
    private double economyRate;
    private int fieldingStats;
    private int label;
}
