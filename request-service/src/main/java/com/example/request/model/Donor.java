package com.example.request.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Donor {
    private String donorId;
    private String userId;
    private String bloodType;
    private LocalDate dateOfBirth;
    private ContactInfo contactInfo;
    private HealthInfo healthInfo;
    private int rewardPoints;

    // Embedded Classes
    @Data
    public static class ContactInfo {
        private String phone;
        private String address;
    }

    @Data
    public static class HealthInfo {
        private boolean eligibilityStatus;
        private LocalDate lastDonationDate;
    }
}