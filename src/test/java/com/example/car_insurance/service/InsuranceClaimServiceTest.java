package com.example.car_insurance.service;

import com.example.car_insurance.model.Car;
import com.example.car_insurance.model.InsuranceClaim;
import com.example.car_insurance.repository.CarRepository;
import com.example.car_insurance.repository.InsuranceClaimRepository;
import com.example.car_insurance.repository.InsurancePolicyRepository;
import com.example.car_insurance.dto.InsuranceClaimDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsuranceClaimServiceTest {

    @Mock
    private InsuranceClaimRepository claimRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private InsurancePolicyRepository policyRepository;

    @InjectMocks
    private InsuranceClaimService claimService;

    @Test
    void createClaimSuccess() {
        Car car = new Car();
        car.setVin("VIN123");

        InsuranceClaim claim = new InsuranceClaim();
        claim.setId(1234L);
        claim.setCar(car);
        claim.setClaimDate(LocalDate.of(2025, 8, 31));
        claim.setDescription("Accident");
        claim.setAmount(new BigDecimal("1000.00"));

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(claimRepository.save(any(InsuranceClaim.class))).thenReturn(claim);

        InsuranceClaimDto.CreateInsuranceClaimRequest request = new InsuranceClaimDto.CreateInsuranceClaimRequest(
                LocalDate.of(2025, 8, 31), "Accident", new BigDecimal("1000.00")
        );

        InsuranceClaim createdClaim = claimService.createInsuranceClaim(1L, request);

        assertNotNull(createdClaim);
        assertEquals(car, createdClaim.getCar());
        assertEquals("Accident", createdClaim.getDescription());
        verify(carRepository).findById(1L);
        verify(claimRepository).save(any(InsuranceClaim.class));
    }

    @Test
    void createClaimWithNonExistentCar() {
        when(carRepository.findById(999L)).thenReturn(Optional.empty());

        InsuranceClaimDto.CreateInsuranceClaimRequest request = new InsuranceClaimDto.CreateInsuranceClaimRequest(
                LocalDate.of(2025, 8, 31), "Accident", new BigDecimal("1000.00")
        );

        assertThrows(RuntimeException.class, () -> {
            claimService.createInsuranceClaim(999L, request);
        });

        verify(carRepository).findById(999L);
        verify(claimRepository, never()).save(any(InsuranceClaim.class));
    }

}