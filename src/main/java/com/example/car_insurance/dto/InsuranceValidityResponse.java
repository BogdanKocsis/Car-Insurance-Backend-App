package com.example.car_insurance.dto;


public record InsuranceValidityResponse(Long carId, String date, boolean valid) {
}