package com.example.car_insurance.web;

import com.example.car_insurance.model.Car;
import com.example.car_insurance.model.InsuranceClaim;
import com.example.car_insurance.service.InsuranceClaimService;
import com.example.car_insurance.controller.InsuranceClaimController;
import com.example.car_insurance.dto.HistoryEventDto;
import com.example.car_insurance.dto.InsuranceClaimDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InsuranceClaimController.class)
class InsuranceClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceClaimService claimService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClaimSuccess() throws Exception {
        Car car = new Car();
        car.setVin("VIN123");

        InsuranceClaim claim = new InsuranceClaim();
        claim.setCar(car);
        claim.setClaimDate(LocalDate.of(2025, 9, 1));
        claim.setDescription("Accident");
        claim.setAmount(new BigDecimal("1000.00"));

        when(claimService.createInsuranceClaim(eq(1L), any(InsuranceClaimDto.CreateInsuranceClaimRequest.class)))
                .thenReturn(claim);

        InsuranceClaimDto.CreateInsuranceClaimRequest request = new InsuranceClaimDto.CreateInsuranceClaimRequest(
                LocalDate.of(2025, 9, 1), "Accident", new BigDecimal("1000.00")
        );

        mockMvc.perform(post("/api/cars/1/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Accident"))
                .andExpect(jsonPath("$.insuranceClaimDate").value("2025-09-01"))
                .andExpect(jsonPath("$.amount").value(1000.00));
    }

    @Test
    void createClaimWithNonExistentCar() throws Exception {
        when(claimService.createInsuranceClaim(eq(999L), any(InsuranceClaimDto.CreateInsuranceClaimRequest.class)))
                .thenThrow(new NoSuchElementException("car not found"));

        InsuranceClaimDto.CreateInsuranceClaimRequest request = new InsuranceClaimDto.CreateInsuranceClaimRequest(
                LocalDate.of(2025, 9, 1), "Accident", new BigDecimal("1000.00")
        );

        mockMvc.perform(post("/api/cars/999/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCarHistorySuccess() throws Exception {
        HistoryEventDto event1 = new HistoryEventDto("POLICY_START", LocalDate.of(2025, 1, 1), "Insurance policy started with Allianz");
        HistoryEventDto event2 = new HistoryEventDto("CLAIM", LocalDate.of(2025, 9, 1), "Claim: Accident - Amount: 1000.00");

        when(claimService.getCarHistory(1L))
                .thenReturn(List.of(event1, event2));

        mockMvc.perform(get("/api/cars/1/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].type").value("POLICY_START"))
                .andExpect(jsonPath("$[1].type").value("CLAIM"));
    }

    @Test
    void getCarHistoryWithNonExistentCar() throws Exception {
        when(claimService.getCarHistory(999L))
                .thenThrow(new NoSuchElementException("car was not found"));

        mockMvc.perform(get("/api/cars/999/history"))
                .andExpect(status().isNotFound());
    }
}