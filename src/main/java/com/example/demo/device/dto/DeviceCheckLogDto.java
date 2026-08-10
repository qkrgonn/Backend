package com.example.demo.device.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DeviceCheckLogDto {
    private Long adminId;
    private String admName;
    private String actionType;
    private LocalDateTime logTime;

    public DeviceCheckLogDto(Long adminId, String admName, String actionType, LocalDateTime logTime) {
        this.adminId = adminId;
        this.admName = admName;
        this.actionType = actionType;
        this.logTime = logTime;
    }
}
