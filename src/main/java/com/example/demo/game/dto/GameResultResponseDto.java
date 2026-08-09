package com.example.demo.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameResultResponseDto {
    private String message;
    private int score;
    private int totalLives;
    private int totalScore;
}
