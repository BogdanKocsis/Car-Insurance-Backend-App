package com.example.car_insurance.dto;

public record CarDto(Long id, String vin, String make, String model, int yearOfManuFacture, Long ownerId, String ownerName, String ownerEmail) {}
