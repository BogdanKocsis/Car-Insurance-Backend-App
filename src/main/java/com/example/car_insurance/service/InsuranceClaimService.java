package com.example.car_insurance.service;

import com.example.car_insurance.exception.ResourceNotFoundException;
import com.example.car_insurance.model.Car;
import com.example.car_insurance.model.InsuranceClaim;
import com.example.car_insurance.model.InsurancePolicy;
import com.example.car_insurance.repository.CarRepository;
import com.example.car_insurance.repository.InsuranceClaimRepository;
import com.example.car_insurance.repository.InsurancePolicyRepository;
import com.example.car_insurance.dto.HistoryEventDto;
import com.example.car_insurance.dto.InsuranceClaimDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class InsuranceClaimService {

    private final InsuranceClaimRepository insuranceClaimRepository;
    private final CarRepository carRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;

    public InsuranceClaimService(InsuranceClaimRepository insuranceClaimRepository, CarRepository carRepository, InsurancePolicyRepository insurancePolicyRepository) {
        this.insuranceClaimRepository = insuranceClaimRepository;
        this.carRepository = carRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    public InsuranceClaim createInsuranceClaim(Long carId, InsuranceClaimDto.CreateInsuranceClaimRequest createInsuranceClaimRequest) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException("Car was not found"));

        InsuranceClaim insuranceClaim = new InsuranceClaim(car, createInsuranceClaimRequest.insuranceClaimDate(), createInsuranceClaimRequest.description(), createInsuranceClaimRequest.amount());
        return insuranceClaimRepository.save(insuranceClaim);
    }

    public List<HistoryEventDto> getCarHistory(Long carId) {
        if(carRepository.findById(carId).isEmpty()) {
            throw new ResourceNotFoundException("Car was not found");
        }

        List<HistoryEventDto> historyEvents = new ArrayList<>();
        List<InsurancePolicy> policies = insurancePolicyRepository.findByCarId(carId);

        for (InsurancePolicy policy : policies) {
            historyEvents.add(new HistoryEventDto("POLICY_START", policy.getStartDate(),
                    "Insurance policy started with " + policy.getProvider()));
            historyEvents.add(new HistoryEventDto("POLICY_END", policy.getEndDate(),
                    "Insurance policy ended with " + policy.getProvider()));
        }

        List<InsuranceClaim> insuranceClaims = insuranceClaimRepository.findByCarIdOrderByClaimDateAsc(carId);
        for (InsuranceClaim insuranceClaim : insuranceClaims) {
            historyEvents.add(new HistoryEventDto("INSURANCE_CLAIM", insuranceClaim.getClaimDate(),
                    "Insurance Claim: " + insuranceClaim.getDescription() + " - Amount: " + insuranceClaim.getAmount()));
        }
        historyEvents.sort(Comparator.comparing(HistoryEventDto::date));
        return historyEvents;
    }

}
