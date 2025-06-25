package com.example.inventory.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Entity // تحديد أن هذا الكلاس هو كيان JPA
@Table(name = "blood_units") // تحديد اسم الجدول في قاعدة البيانات
@Data // من Lombok: ينشئ getters و setters و toString و equals و hashCode
@NoArgsConstructor // من Lombok: ينشئ مُنشئ بدون وسائط
@AllArgsConstructor // من Lombok: ينشئ مُنشئ بجميع الوسائط
@Builder // من Lombok: ينشئ مُنشئ Builder لإنشاء الكائنات بسهولة
public class BloodUnit {

    @Id // تحديد المفتاح الأساسي للكيان
    @GeneratedValue(strategy = GenerationType.UUID) // توليد UUID تلقائيًا للمفتاح الأساسي
    private UUID id;

    @Enumerated(EnumType.STRING) // تخزين التعداد كسلسلة نصية
    private BloodType bloodType;

    private LocalDate collectionDate; // تاريخ جمع الوحدة

    private LocalDate expiryDate; // تاريخ انتهاء صلاحية الوحدة

    private String hospitalId; // معرف المستشفى الذي جمع الوحدة

    @Enumerated(EnumType.STRING) // تخزين التعداد كسلسلة نصية
    private Status status; // حالة الوحدة (متاحة، مستخدمة، تالفة، إلخ)

    // تعريف التعداد لأنواع الدم (يمكن أن يكون هو نفسه في Donor Service)
    public enum BloodType {
        A_POS, A_NEG, B_POS, B_NEG, AB_POS, AB_NEG, O_POS, O_NEG
    }

    // تعريف التعداد لحالة وحدة الدم
    public enum Status {
        AVAILABLE, USED, DAMAGED, EXPIRED, IN_TRANSIT
    }
}