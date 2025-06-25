package com.example.request.controller;

import com.example.request.model.Donor;
import com.example.request.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("BloodBankApi/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/potential-donors")
    public List<Donor> getPotentialDonors(@RequestParam String bloodType) {

        return requestService.findPotentialDonors(bloodType);
    }
}