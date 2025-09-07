package com.example.carins.web;

import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Test
    void insuranceValidWithValidDate() throws Exception {
        when(carService.carExists(1L)).thenReturn(true);
        when(carService.isInsuranceValid(1L, java.time.LocalDate.of(2025, 9, 1))).thenReturn(true);

        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2025-09-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.date").value("2025-09-01"))
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    void insuranceValidWithInvalidDateFormat() throws Exception {
        when(carService.carExists(1L)).thenReturn(true);

        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "invalid-date"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid date format. Use YYYY-MM-DD"));
    }

    @Test
    void insuranceValidWithDateBefore1900() throws Exception {
        when(carService.carExists(1L)).thenReturn(true);

        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "1899-12-31"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Date cannot be before 1900-01-01"));
    }

    @Test
    void insuranceValidWithDateAfter2100() throws Exception {
        when(carService.carExists(1L)).thenReturn(true);

        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2101-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Date cannot be after 2100-12-31"));
    }

    @Test
    void insuranceValidWithNonExistentCar() throws Exception {
        when(carService.carExists(999L)).thenReturn(false);

        mockMvc.perform(get("/api/cars/999/insurance-valid")
                        .param("date", "2025-06-01"))
                .andExpect(status().isNotFound());
    }
}