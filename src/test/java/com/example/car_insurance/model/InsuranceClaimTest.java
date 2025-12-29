package com.example.car_insurance.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InsuranceClaimTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validClaim() {
        Car car = new Car();
        car.setVin("VIN123");

        InsuranceClaim claim = new InsuranceClaim();
        claim.setCar(car);
        claim.setClaimDate(LocalDate.of(2025, 8, 31));
        claim.setDescription("Accident");
        claim.setAmount(new BigDecimal("1000.00"));

        Set<ConstraintViolation<InsuranceClaim>> violations = validator.validate(claim);
        assertTrue(violations.isEmpty());
    }


    @Test
    void invalidClaimWithoutDescription() {
        Car car = new Car();
        car.setVin("VIN123");

        InsuranceClaim claim = new InsuranceClaim();
        claim.setCar(car);
        claim.setClaimDate(LocalDate.of(2025, 8, 31));
        claim.setAmount(new BigDecimal("1000.00"));


        Set<ConstraintViolation<InsuranceClaim>> violations = validator.validate(claim);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void invalidClaimWithNegativeAmount() {
        Car car = new Car();
        car.setVin("VIN123");

        InsuranceClaim claim = new InsuranceClaim();
        claim.setCar(car);
        claim.setClaimDate(LocalDate.of(2025, 8, 31));
        claim.setDescription("Accident");
        claim.setAmount(new BigDecimal("-100.00"));

        Set<ConstraintViolation<InsuranceClaim>> violations = validator.validate(claim);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }

}