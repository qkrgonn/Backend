package com.example.demo.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {
    private Long logId;
    private String adminName;
    private String actionType;
    private LocalDateTime logTime;
    private Long deviceId;
    private String schoolName;
}
