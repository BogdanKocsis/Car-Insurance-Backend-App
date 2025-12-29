package com.example.car_insurance.repository;

import com.example.car_insurance.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @EntityGraph(attributePaths = {"owner"})
    List<Car> findAll();

    boolean existsByVin(String vin);
}