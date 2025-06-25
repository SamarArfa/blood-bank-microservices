package com.example.donor.controller;

import com.example.donor.model.Donor;
import com.example.donor.repository.DonorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.MediaType;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController // تحديد أن هذا الكلاس هو متحكم REST
@RequestMapping("BloodBankApi/donors") // تعيين المسار الأساسي لجميع نقاط النهاية في هذا المتحكم
public class DonorController {

    @Autowired // حقن مثيل DonorRepository تلقائيًا
    private DonorRepository donorRepository;

    // 1. Create Donor Profile (POST /BloodBankApi/donors)
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createDonor(@Valid @RequestBody Donor donor) {
        // التحقق مما إذا كان userId موجودًا بالفعل لمنع التكرار (409 Conflict)
        if (donorRepository.findByUserId(donor.getUserId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Donor with this userId already exists.");
        }

        // تهيئة حقول جديدة
        donor.setDonorId(UUID.randomUUID()); // توليد معرف متبرع جديد
        donor.setDonationHistory(new java.util.ArrayList<>()); // تهيئة سجل التبرعات
        donor.setRewardPoints(0); // تعيين نقاط المكافأة الأولية

        Donor savedDonor = donorRepository.save(donor); // حفظ المتبرع في قاعدة البيانات

        // بناء عنوان URI للمورد الذي تم إنشاؤه حديثًا
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{donorId}")
                .buildAndExpand(savedDonor.getDonorId())
                .toUri();

        // إرجاع استجابة 201 Created مع رأس الموقع والمورد الذي تم إنشاؤه
        return ResponseEntity.created(location).body(savedDonor);
    }

    // 2. Read Donor Profile (GET /BloodBankApi/donors/{donorId})
    @GetMapping(value = "/{donorId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Donor> getDonorById(@PathVariable UUID donorId) {
        Optional<Donor> donor = donorRepository.findById(donorId);
        // إذا تم العثور على المتبرع، إرجاع 200 OK مع بيانات المتبرع، وإلا إرجاع 404 Not Found
        return donor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Donor>> getDonorsFiltered(
            @RequestParam(required = false) String bloodType,
            @RequestParam(required = false) Boolean eligibilityStatus) {

        List<Donor> donors;

        if (bloodType != null && eligibilityStatus != null) {
            // تحويل النص إلى BloodType enum
            Donor.BloodType type = parseBloodType(bloodType);
            donors = donorRepository.findByBloodTypeAndHealthInfo_EligibilityStatus(type, eligibilityStatus);
        } else if (bloodType != null) {
            Donor.BloodType type = parseBloodType(bloodType);
            donors = donorRepository.findAll().stream()
                    .filter(d -> d.getBloodType() == type)
                    .collect(Collectors.toList());
        } else if (eligibilityStatus != null) {
            donors = donorRepository.findAll().stream()
                    .filter(d -> d.getHealthInfo().isEligibilityStatus() == eligibilityStatus)
                    .collect(Collectors.toList());
        } else {
            donors = donorRepository.findAll();
        }

        return ResponseEntity.ok(donors);
    }

    // وظيفة مساعدة لتحويل "B-" إلى B_NEG
    private Donor.BloodType parseBloodType(String bloodType) {
        return switch (bloodType.toUpperCase()) {
            case "A+", "A_POS" -> Donor.BloodType.A_POS;
            case "A-", "A_NEG" -> Donor.BloodType.A_NEG;
            case "B+", "B_POS" -> Donor.BloodType.B_POS;
            case "B-", "B_NEG" -> Donor.BloodType.B_NEG;
            case "AB+", "AB_POS" -> Donor.BloodType.AB_POS;
            case "AB-", "AB_NEG" -> Donor.BloodType.AB_NEG;
            case "O+", "O_POS" -> Donor.BloodType.O_POS;
            case "O-", "O_NEG" -> Donor.BloodType.O_NEG;
            default -> throw new IllegalArgumentException("Invalid blood type: " + bloodType);
        };
    }
    // Read Donors by Blood Type and Eligibility Status (GET /BloodBankApi/donors?bloodType=O-&eligibilityStatus=true)
//    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<List<Donor>> getDonorsFiltered(
//            @RequestParam(required = false) Donor.BloodType bloodType,
//            @RequestParam(required = false) Boolean eligibilityStatus) {
//
//        List<Donor> donors;
//        if (bloodType != null && eligibilityStatus != null) {
//            // البحث عن طريق نوع الدم وحالة الأهلية
//            donors = donorRepository.findByBloodTypeAndHealthInfo_EligibilityStatus(bloodType, eligibilityStatus);
//        } else if (bloodType != null) {
//            // البحث عن طريق نوع الدم فقط
//            donors = donorRepository.findAll().stream()
//                    .filter(d -> d.getBloodType().equals(bloodType))
//                    .collect(Collectors.toList());
//        } else if (eligibilityStatus != null) {
//            // البحث عن طريق حالة الأهلية فقط
//            donors = donorRepository.findAll().stream()
//                    .filter(d -> d.getHealthInfo().isEligibilityStatus() == eligibilityStatus)
//                    .collect(Collectors.toList());
//        } else {
//            // إذا لم يتم توفير معلمات، جلب جميع المتبرعين
//            donors = donorRepository.findAll();
//        }
//        return ResponseEntity.ok(donors);
//    }

    // 3. Update Donor Profile (PUT /BloodBankApi/donors/{donorId})
    @PutMapping(value = "/{donorId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Donor> updateDonor(@PathVariable UUID donorId, @Valid @RequestBody Donor updatedDonor) {
        return donorRepository.findById(donorId)
                .map(existingDonor -> {
                    // تحديث جميع الحقول من الكائن الوارد
                    existingDonor.setUserId(updatedDonor.getUserId());
                    existingDonor.setBloodType(updatedDonor.getBloodType());
                    existingDonor.setContactInfo(updatedDonor.getContactInfo());
                    existingDonor.setDateOfBirth(updatedDonor.getDateOfBirth());
                    existingDonor.setHealthInfo(updatedDonor.getHealthInfo());
                    existingDonor.setRewardPoints(updatedDonor.getRewardPoints());
                    // لا تقم بتحديث donationHistory مباشرة من هنا، بل من خلال عملية تبرع
                    // existingDonor.setDonationHistory(updatedDonor.getDonationHistory());

                    Donor savedDonor = donorRepository.save(existingDonor);
                    return ResponseEntity.ok(savedDonor); // إرجاع 200 OK مع المتبرع المحدث
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // إذا لم يتم العثور على المتبرع، إرجاع 404 Not Found
    }

    // 4. Delete Donor Profile (DELETE /BloodBankApi/donors/{donorId})
    @DeleteMapping("/{donorId}")
    public ResponseEntity<Void> deleteDonor(@PathVariable UUID donorId) {
        if (donorRepository.existsById(donorId)) {
            donorRepository.deleteById(donorId);
            return ResponseEntity.noContent().build(); // إرجاع 204 No Content للنجاح
        } else {
            return ResponseEntity.notFound().build(); // إرجاع 404 Not Found إذا لم يتم العثور على المتبرع
        }
    }


    @GetMapping
    public ResponseEntity<List<Donor>> findEligibleDonors(
            @RequestParam String bloodType,
            @RequestParam boolean eligibilityStatus) {
        return ResponseEntity.ok(donorRepository.findByBloodTypeAndHealthInfo_EligibilityStatus(Donor.BloodType.valueOf(bloodType), eligibilityStatus));
    }
}
