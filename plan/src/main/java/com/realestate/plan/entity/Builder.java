package com.realestate.plan.entity;

import com.realestate.plan.enums.BuilderStatus;
import com.realestate.plan.enums.BuilderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "builders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@lombok.Builder
public class Builder {

    // ===================== PRIMARY KEY =====================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "builder_code", unique = true, nullable = false, length = 20)
    private String builderCode;

    // ===================== BASIC INFORMATION =====================
    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    @Column(name = "brand_name", length = 200)
    private String brandName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private BuilderType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BuilderStatus status;

    @Column(name = "established_year")
    private Integer establishedYear;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "website_url", length = 300)
    private String websiteUrl;

    // ===================== REGISTRATION & LEGAL =====================
    @Column(name = "registration_number", unique = true, length = 100)
    private String registrationNumber;

    @Column(name = "gst_number", unique = true, length = 20)
    private String gstNumber;

    @Column(name = "pan_number", unique = true, length = 15)
    private String panNumber;

    @Column(name = "rera_number", unique = true, length = 100)
    private String reraNumber;

    @Column(name = "rera_state_code", length = 10)
    private String reraStateCode;

    @Column(name = "rera_expiry_date")
    private LocalDate reraExpiryDate;

    @Column(name = "license_number", length = 100)
    private String licenseNumber;

    // ===================== CONTACT INFORMATION =====================
    @Column(name = "primary_phone", nullable = false, length = 15)
    private String primaryPhone;

    @Column(name = "secondary_phone", length = 15)
    private String secondaryPhone;

    @Column(name = "email_id", nullable = false, unique = true, length = 100)
    private String emailId;

    @Column(name = "alternate_email", length = 100)
    private String alternateEmail;

    // ===================== ADDRESS =====================
    @Column(name = "address_line1", length = 300)
    private String addressLine1;

    @Column(name = "address_line2", length = 300)
    private String addressLine2;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    @lombok.Builder.Default
    private String country = "India";

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // ===================== KEY PERSON / OWNER =====================
    @Column(name = "owner_name", length = 150)
    private String ownerName;

    @Column(name = "owner_phone", length = 15)
    private String ownerPhone;

    @Column(name = "owner_email", length = 100)
    private String ownerEmail;

    @Column(name = "owner_pan", length = 15)
    private String ownerPan;

    @Column(name = "owner_designation", length = 50)
    private String ownerDesignation;

    // ===================== FINANCIAL INFORMATION =====================
    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_account_number", length = 30)
    private String bankAccountNumber;

    @Column(name = "ifsc_code", length = 15)
    private String ifscCode;

    @Column(name = "bank_branch", length = 100)
    private String bankBranch;

    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "payment_terms", length = 100)
    private String paymentTerms;

    // ===================== RATINGS =====================
    @Column(name = "rating")
    @lombok.Builder.Default
    private Double rating = 0.0;

    @Column(name = "total_reviews")
    @lombok.Builder.Default
    private Integer totalReviews = 0;

    // ===================== AUDIT FIELDS =====================
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "is_deleted")
    @lombok.Builder.Default
    private Boolean isDeleted = false;
}
