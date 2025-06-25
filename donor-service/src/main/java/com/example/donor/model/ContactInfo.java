package com.example.donor.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable // تحديد أن هذا الكلاس يمكن تضمينه في كيانات JPA أخرى
@Data // من Lombok: ينشئ getters و setters و toString و equals و hashCode
@NoArgsConstructor // من Lombok: ينشئ مُنشئ بدون وسائط
@AllArgsConstructor // من Lombok: ينشئ مُنشئ بجميع الوسائط
public class ContactInfo {
    private String phone;
    private String address;
}
