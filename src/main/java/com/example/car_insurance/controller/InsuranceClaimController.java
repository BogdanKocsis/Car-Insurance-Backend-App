package com.example.car_insurance.controller;


import com.example.car_insurance.model.InsuranceClaim;
import com.example.car_insurance.service.InsuranceClaimService;
import com.example.car_insurance.dto.HistoryEventDto;
import com.example.car_insurance.dto.InsuranceClaimDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cars")
public class InsuranceClaimController {

    private final InsuranceClaimService insuranceClaimService;

    public InsuranceClaimController(InsuranceClaimService insuranceClaimService) {
        this.insuranceClaimService = insuranceClaimService;
    }

    @PostMapping("/{carId}/claims")
    public ResponseEntity<InsuranceClaimDto.InsuranceClaimResponse> createClaim(
            @PathVariable Long carId,
            @Valid @RequestBody InsuranceClaimDto.CreateInsuranceClaimRequest request) {
        try {
            InsuranceClaim claim = insuranceClaimService.createInsuranceClaim(carId, request);

            InsuranceClaimDto.InsuranceClaimResponse response = new InsuranceClaimDto.InsuranceClaimResponse(
                    claim.getId(), carId, claim.getClaimDate(),
                    claim.getDescription(), claim.getAmount()
            );

            URI location = URI.create("/api/cars/" + carId + "/claims/" + claim.getId());
            return ResponseEntity.created(location).body(response);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    @GetMapping("/{carId}/history")
    public ResponseEntity<List<HistoryEventDto>> getCarHistory(
            @PathVariable Long carId) {
        try {
            List<HistoryEventDto> events = insuranceClaimService.getCarHistory(carId);
            return ResponseEntity.ok(events);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
