package com.example.request.service;

import com.example.request.client.DonorClient;
import com.example.request.model.Donor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private DonorClient donorClient;

    public List<Donor> findPotentialDonors(String bloodType) {
        return donorClient.findEligibleDonors(bloodType, true);
    }
}