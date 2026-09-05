package com.example.demo.device.dto;

public record PetInputResult(
        String eventId,
        String status,
        int creditedCount,
        Integer currentLives,
        Integer totalRecycleCount) {
}
