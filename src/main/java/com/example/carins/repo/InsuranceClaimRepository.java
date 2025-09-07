package com.example.carins.repo;

import com.example.carins.model.InsuranceClaim;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceClaimRepository extends CrudRepository<InsuranceClaim, Long> {

    @Query("SELECT claim FROM InsuranceClaim claim WHERE claim.car.id = :carId ORDER BY claim.claimDate ASC")
    List<InsuranceClaim> findByCarIdOrderByClaimDateAsc(@Param("carId") Long carId);
}
