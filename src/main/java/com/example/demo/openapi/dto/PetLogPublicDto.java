package com.example.demo.openapi.dto;

import java.time.LocalDateTime;

public record PetLogPublicDto(
    LocalDateTime inputTime,
    Integer inputCount,
    Long schoolId,
    Long deviceId
) {}
