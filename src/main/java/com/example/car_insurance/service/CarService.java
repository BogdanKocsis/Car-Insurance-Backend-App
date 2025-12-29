package com.example.car_insurance.service;

import com.example.car_insurance.model.Car;
import com.example.car_insurance.repository.CarRepository;
import com.example.car_insurance.repository.InsurancePolicyRepository;
import com.example.car_insurance.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;
    private final OwnerRepository ownerRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository, OwnerRepository ownerRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
        this.ownerRepository = ownerRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carId == null || date == null) return false;
        if (!carRepository.existsById(carId)) return false;

        return policyRepository.existsActiveOnDate(carId, date);
    }

    public boolean carExists(Long carId) {
        return carRepository.existsById(carId);
    }

    public boolean vinExists(String vin) {
        return carRepository.existsByVin(vin);
    }

    public Car saveCar(Car car) {
        if(car.getOwner() == null){
            var defaultOwner = ownerRepository.findById(1L).orElseThrow();
            car.setOwner(defaultOwner);
        }
        return carRepository.save(car);
    }
}
