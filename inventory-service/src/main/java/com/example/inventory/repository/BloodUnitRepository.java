package com.example.inventory.repository;

import com.example.inventory.model.BloodUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository // تحديد أن هذه الواجهة هي مستودع بيانات
public interface BloodUnitRepository extends JpaRepository<BloodUnit, UUID> {
    // يمكنك إضافة طرق بحث مخصصة هنا إذا لزم الأمر، مثل findByBloodTypeAndStatus
    List<BloodUnit> findByBloodTypeAndStatus(BloodUnit.BloodType bloodType, BloodUnit.Status status);
}