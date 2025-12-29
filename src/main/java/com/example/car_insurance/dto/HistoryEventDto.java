package com.example.car_insurance.dto;

import java.time.LocalDate;

public record HistoryEventDto(
        String type,
        LocalDate date,
        String details
) {
}
