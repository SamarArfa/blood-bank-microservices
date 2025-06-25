package com.example.donor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity // تحديد أن هذا الكلاس هو كيان JPA
@Table(name = "donors") // تحديد اسم الجدول في قاعدة البيانات
@Data // من Lombok: ينشئ getters و setters و toString و equals و hashCode
@NoArgsConstructor // من Lombok: ينشئ مُنشئ بدون وسائط
@AllArgsConstructor // من Lombok: ينشئ مُنشئ بجميع الوسائط
@Builder // من Lombok: ينشئ مُنشئ Builder لإنشاء الكائنات بسهولة
public class Donor {

    @Id // تحديد المفتاح الأساسي للكيان
    @GeneratedValue(strategy = GenerationType.UUID) // توليد UUID تلقائيًا للمفتاح الأساسي
    private UUID donorId;

    @Column(unique = true, nullable = false) // تحديد أن userId فريد وغير قابل للقيمة الفارغة
    private String userId; // معرف المستخدم من نظام المصادقة الخارجي

    @Enumerated(EnumType.STRING) // تخزين التعداد كسلسلة نصية
    private BloodType bloodType;

    @Embedded // تضمين كائن ContactInfo كجزء من كيان Donor
    private ContactInfo contactInfo;

    private LocalDate dateOfBirth;

    @Embedded // تضمين كائن HealthInfo كجزء من كيان Donor
    private HealthInfo healthInfo;

    @ElementCollection // لتخزين مجموعة من العناصر (سجلات التبرع)
    @CollectionTable(name = "donation_history", joinColumns = @JoinColumn(name = "donor_id"))
    @Column(name = "donation_date") // اسم العمود الذي سيحتوي على تاريخ التبرع
    private List<LocalDate> donationHistory = new ArrayList<>(); // سجل تواريخ التبرع

    private int rewardPoints; // نقاط المكافأة

    // تعريف التعداد لأنواع الدم
    public enum BloodType {
        A_POS, A_NEG, B_POS, B_NEG, AB_POS, AB_NEG, O_POS, O_NEG
    }
}

