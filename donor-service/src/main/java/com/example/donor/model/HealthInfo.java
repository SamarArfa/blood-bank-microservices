package com.example.donor.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable // تحديد أن هذا الكلاس يمكن تضمينه في كيانات JPA أخرى
@Data // من Lombok: ينشئ getters و setters و toString و equals و hashCode
@NoArgsConstructor // من Lombok: ينشئ مُنشئ بدون وسائط
@AllArgsConstructor // من Lombok: ينشئ مُنشئ بجميع الوسائط
public class HealthInfo {
    private boolean eligibilityStatus; // حالة الأهلية للتبرع
    private LocalDate lastDonationDate; // تاريخ آخر تبرع
    // يمكنك إضافة المزيد من الحقول المتعلقة بالصحة هنا
}