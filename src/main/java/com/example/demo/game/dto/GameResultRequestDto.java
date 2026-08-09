package com.example.demo.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResultRequestDto {
    private String userId;
    private String classificationResult;   // YOLO 결과 ("CLEAN", "NONPET", "LABEL", "DIRTY" 등)
    private int livesUsed;
    private String playDate;
    private int score;
    private int playTime;                // 플레이 시간(초 단위)
}
