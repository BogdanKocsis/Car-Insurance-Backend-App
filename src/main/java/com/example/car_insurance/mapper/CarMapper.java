package com.example.car_insurance.mapper;

import com.example.car_insurance.dto.CarDto;
import com.example.car_insurance.model.Car;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {
    public CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(
                c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null
        );
    }
}