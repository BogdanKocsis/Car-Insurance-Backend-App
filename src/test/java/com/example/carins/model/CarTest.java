package com.example.carins.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validCar() {
        Owner owner = new Owner();
        owner.setName("Test Owner");

        Car car = new Car();
        car.setVin("VIN123456789");
        car.setMake("Test Make");
        car.setModel("Test Model");
        car.setYearOfManufacture(2018);
        car.setOwner(owner);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidCarWithShortVin() {
        Owner owner = new Owner();
        owner.setName("Test Owner");

        Car car = new Car();
        car.setVin("1234");
        car.setMake("Test Make");
        car.setModel("Test Model");
        car.setYearOfManufacture(2018);
        car.setOwner(owner);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("vin")));
    }


}