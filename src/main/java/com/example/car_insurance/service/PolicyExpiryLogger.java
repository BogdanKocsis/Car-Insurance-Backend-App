package com.example.car_insurance.service;

import com.example.car_insurance.model.InsurancePolicy;
import com.example.car_insurance.repository.InsurancePolicyRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class PolicyExpiryLogger {
    private static final Logger log = LoggerFactory.getLogger(PolicyExpiryLogger.class);
    private final InsurancePolicyRepository insurancePolicyRepository;


    public PolicyExpiryLogger(InsurancePolicyRepository insurancePolicyRepository) {
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    @Transactional
    public void logExpiredPolicies() {
        LocalDateTime now = LocalDateTime.now();
        if (now.toLocalTime().isAfter(LocalTime.MIDNIGHT))
            return;

        LocalDate expiredDate = now.toLocalDate().minusDays(1);
        var policies = insurancePolicyRepository.findByExpiryNotifiedFalseAndEndDate(expiredDate);

        for (InsurancePolicy policy : policies) {
            log.info("Policy {} for car {} expired on {}", policy.getId(), policy.getCar().getId(), policy.getEndDate());
            policy.setExpiryNotified(true);
        }
    }
}
