package com.example.carins.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InsuranceClaimDto {
    public record CreateInsuranceClaimRequest(
            @NotNull @PastOrPresent(message = "Insurance Claim Date cannot be in the future") LocalDate insuranceClaimDate,
            @NotBlank String description,
            @NotNull @Positive BigDecimal amount
    ) {
    }

    public record InsuranceClaimResponse(
            Long id,
            Long carId,
            LocalDate insuranceClaimDate,
            String description,
            BigDecimal amount
    ) {
    }
}
