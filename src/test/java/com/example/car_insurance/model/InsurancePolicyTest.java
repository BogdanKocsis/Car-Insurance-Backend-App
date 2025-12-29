package com.example.car_insurance.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validInsurancePolicy() {
        Owner owner = new Owner();
        owner.setName("Test Owner");

        Car car = new Car("VIN123456789", "Test Make", "Test Model", 2020, owner);

        InsurancePolicy policy = new InsurancePolicy();
        policy.setCar(car);
        policy.setProvider("Allianz");
        policy.setStartDate(LocalDate.of(2025, 1, 1));
        policy.setEndDate(LocalDate.of(2025, 12, 31));

        Set<ConstraintViolation<InsurancePolicy>> violations = validator.validate(policy);
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidInsurancePolicyWithoutEndDate() {
        Owner owner = new Owner();
        owner.setName("Test Owner");

        Car car = new Car("VIN123456789", "Test Make", "Test Model", 2020, owner);

        InsurancePolicy policy = new InsurancePolicy();
        policy.setCar(car);
        policy.setProvider("Allianz");
        policy.setStartDate(LocalDate.of(2025, 1, 1));
        // endDate null

        Set<ConstraintViolation<InsurancePolicy>> violations = validator.validate(policy);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("endDate")));
    }
    @Test
    void testExpiryNotifiedField() {
        Owner owner = new Owner();
        owner.setName("Test Owner");

        Car car = new Car("VIN123456789", "Test Make", "Test Model", 2020, owner);

        InsurancePolicy policy = new InsurancePolicy();
        policy.setCar(car);
        policy.setProvider("Allianz");
        policy.setStartDate(LocalDate.of(2025, 1, 1));
        policy.setEndDate(LocalDate.of(2025, 12, 31));

        assertFalse(policy.isExpiryNotified());
    }

}