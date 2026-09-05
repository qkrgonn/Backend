package com.example.demo.device.event;

import com.example.demo.user.dto.LivesDto;

public record LivesUpdatedEvent(String userId, LivesDto payload) {
}
