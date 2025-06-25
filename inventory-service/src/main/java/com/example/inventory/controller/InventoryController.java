package com.example.inventory.controller;

import com.example.inventory.model.BloodUnit;
import com.example.inventory.repository.BloodUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController // تحديد أن هذا الكلاس هو متحكم REST
@RequestMapping("BloodBankApi/inventory/units") // تعيين المسار الأساسي لجميع نقاط النهاية في هذا المتحكم
public class InventoryController {

    @Autowired // حقن مثيل BloodUnitRepository تلقائيًا
    private BloodUnitRepository bloodUnitRepository;

    // 1. Create Inventory (POST /BloodBankApi/inventory/units)
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<BloodUnit> createBloodUnit(@RequestBody BloodUnit bloodUnit) {
        bloodUnit.setId(UUID.randomUUID()); // توليد معرف فريد لوحدة الدم

        BloodUnit savedBloodUnit = bloodUnitRepository.save(bloodUnit); // حفظ وحدة الدم في قاعدة البيانات

        // بناء عنوان URI للمورد الذي تم إنشاؤه حديثًا
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBloodUnit.getId())
                .toUri();

        // إرجاع استجابة 201 Created مع رأس الموقع والمورد الذي تم إنشاؤه
        return ResponseEntity.created(location).body(savedBloodUnit);
    }

    // 2. Read Inventory (GET /BloodBankApi/inventory/units/{unit-uuid-1})
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<BloodUnit> getBloodUnitById(@PathVariable UUID id) {
        Optional<BloodUnit> bloodUnit = bloodUnitRepository.findById(id);
        // إذا تم العثور على وحدة الدم، إرجاع 200 OK مع بيانات الوحدة، وإلا إرجاع 404 Not Found
        return bloodUnit.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Optional: Get all blood units or filter by type/status
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<BloodUnit>> getAllBloodUnits(
            @RequestParam(required = false) BloodUnit.BloodType bloodType,
            @RequestParam(required = false) BloodUnit.Status status) {

        List<BloodUnit> bloodUnits;
        if (bloodType != null && status != null) {
            bloodUnits = bloodUnitRepository.findByBloodTypeAndStatus(bloodType, status);
        } else if (bloodType != null) {
            bloodUnits = bloodUnitRepository.findAll().stream()
                    .filter(unit -> unit.getBloodType().equals(bloodType))
                    .collect(java.util.stream.Collectors.toList());
        } else if (status != null) {
            bloodUnits = bloodUnitRepository.findAll().stream()
                    .filter(unit -> unit.getStatus().equals(status))
                    .collect(java.util.stream.Collectors.toList());
        } else {
            bloodUnits = bloodUnitRepository.findAll();
        }
        return ResponseEntity.ok(bloodUnits);
    }

    // Optional: Update Blood Unit Status (PATCH) - as an example
    @PatchMapping(value = "/{id}/status",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<BloodUnit> updateBloodUnitStatus(
            @PathVariable UUID id,
            @RequestParam BloodUnit.Status newStatus) {

        return bloodUnitRepository.findById(id)
                .map(bloodUnit -> {
                    bloodUnit.setStatus(newStatus);
                    return ResponseEntity.ok(bloodUnitRepository.save(bloodUnit));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
