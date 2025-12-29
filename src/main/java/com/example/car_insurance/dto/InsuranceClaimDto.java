package com.example.car_insurance.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InsuranceClaimDto {
    public record CreateInsuranceClaimRequest(
            @NotNull @PastOrPresent(message = "Insurance Claim Date cannot be in the future") LocalDate insuranceClaimDate,
            @NotBlank @Size(max = 2500) String description,
            @NotNull @Positive @Digits(integer = 10, fraction = 2) BigDecimal amount
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
