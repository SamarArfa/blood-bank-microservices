package com.example.request.client;

import com.example.request.model.Donor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "donor-service")
public interface DonorClient {

    @GetMapping("/BloodBankApi/donors")
    List<Donor> findEligibleDonors(
            @RequestParam String bloodType,
            @RequestParam boolean eligibilityStatus);
}