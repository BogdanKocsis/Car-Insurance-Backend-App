package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.HistoryEventDto;
import com.example.carins.web.dto.InsuranceClaimDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

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

    public InsuranceClaim createInsuranceClaim(Long cardId, InsuranceClaimDto.CreateInsuranceClaimRequest createInsuranceClaimRequest) {
        Car car = carRepository.findById(cardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car was not found"));

        InsuranceClaim insuranceClaim = new InsuranceClaim(car, createInsuranceClaimRequest.insuranceClaimDate(), createInsuranceClaimRequest.description(), createInsuranceClaimRequest.amount());
        return insuranceClaimRepository.save(insuranceClaim);
    }

    public List<HistoryEventDto> getCarHistory(Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new NoSuchElementException("Car was not found"));

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
