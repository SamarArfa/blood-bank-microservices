package com.example.donor.repository;

import com.example.donor.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository // تحديد أن هذه الواجهة هي مستودع بيانات
public interface DonorRepository extends JpaRepository<Donor, UUID> {
    Optional<Donor> findByUserId(String userId); // البحث عن متبرع بواسطة معرف المستخدم
    List<Donor> findByBloodTypeAndHealthInfo_EligibilityStatus(Donor.BloodType bloodType, boolean eligibilityStatus); // البحث عن متبرعين حسب نوع الدم وحالة الأهلية
}