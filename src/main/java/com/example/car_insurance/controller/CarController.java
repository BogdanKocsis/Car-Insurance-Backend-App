package com.example.car_insurance.controller;

import com.example.car_insurance.dto.CreateCarRequest;
import com.example.car_insurance.dto.InsuranceValidityResponse;
import com.example.car_insurance.exception.ResourceNotFoundException;
import com.example.car_insurance.mapper.CarMapper;
import com.example.car_insurance.model.Car;
import com.example.car_insurance.service.CarService;
import com.example.car_insurance.dto.CarDto;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;
    private final CarMapper carMapper;

    public CarController(CarService service, CarMapper carMapper) {
        this.service = service;
        this.carMapper = carMapper;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(carMapper::toDto).toList();
    }

    @PostMapping("/cars")
    public ResponseEntity<?> createCar(@Valid @RequestBody CreateCarRequest carRequest) {
        if (service.vinExists(carRequest.vin()))
            return ResponseEntity.badRequest().body(new ResourceNotFoundException("Car with VIN " + carRequest.vin() + " already exists"));

        Car car = new Car(carRequest.vin(), carRequest.make(), carRequest.model(), carRequest.yearOfManufacture(), null);
        Car savedCar = service.saveCar(car);
        return ResponseEntity.created(URI.create("/api/cars/" + savedCar.getId())).build();
    }


    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (!service.carExists(carId)) return ResponseEntity.notFound().build();

        if (date.isBefore(LocalDate.of(1900, 1, 1)))
            return ResponseEntity.badRequest().body(new ResourceNotFoundException("Date cannot be before 1900-01-01"));
        if (date.isAfter(LocalDate.of(2100, 12, 31)))
            return ResponseEntity.badRequest().body(new ResourceNotFoundException("Date cannot be after 2100-12-31"));

        boolean valid = service.isInsuranceValid(carId, date);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, date.toString(), valid));
    }

}
