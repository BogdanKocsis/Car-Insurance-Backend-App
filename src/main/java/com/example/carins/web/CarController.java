package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @PostMapping("/cars")
    public ResponseEntity<?> createCar(@Valid @RequestBody CreateCarRequest carRequest) {
        if (service.vinExists(carRequest.vin))
            return ResponseEntity.badRequest().body(new ErrorResponse("Car with VIN " + carRequest.vin() + " already exists"));

        Car car = new Car(carRequest.vin(), carRequest.make(), carRequest.model(), carRequest.yearOfManufacture(), null);
        Car savedCar = service.saveCar(car);
        return ResponseEntity.created(URI.create("/api/cars/" + savedCar.getId())).build();
    }


    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {
        if (!service.carExists(carId)) return ResponseEntity.notFound().build();
        try {
            LocalDate d = LocalDate.parse(date);

            if (d.isBefore(LocalDate.of(1900, 1, 1))) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Date cannot be before 1900-01-01"));
            }

            if (d.isAfter(LocalDate.of(2100, 12, 31))) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Date cannot be after 2100-12-31"));
            }

            boolean valid = service.isInsuranceValid(carId, d);
            return ResponseEntity.ok(new InsuranceValidityResponse(carId, d.toString(), valid));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid date format. Use YYYY-MM-DD"));
        }
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {
    }

    public record ErrorResponse(String message) {
    }

    public record CreateCarRequest(
            @NotBlank @Size(min = 5, max = 32) String vin,
            String make,
            String model,
            int yearOfManufacture
    ) {
    }
}
