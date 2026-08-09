package com.example.demo.openapi.dto;

import java.time.LocalDate;

public record DailyStatDto(LocalDate date, Long totalCount) {}
