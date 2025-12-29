package com.example.car_insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCarRequest(
        @NotBlank @Size(min = 5, max = 32) String vin,
        String make,
        String model,
        int yearOfManufacture
) {
}