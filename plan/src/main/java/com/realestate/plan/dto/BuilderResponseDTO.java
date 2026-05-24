package com.realestate.plan.dto;

import com.realestate.plan.enums.BuilderStatus;
import com.realestate.plan.enums.BuilderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuilderResponseDTO {

    private Long id;
    private String builderCode;

    // Basic Information
    private String companyName;
    private String brandName;
    private BuilderType type;
    private BuilderStatus status;
    private Integer establishedYear;
    private String description;
    private String logoUrl;
    private String websiteUrl;

    // Registration & Legal
    private String registrationNumber;
    private String gstNumber;
    private String panNumber;
    private String reraNumber;
    private String reraStateCode;
    private LocalDate reraExpiryDate;
    private String licenseNumber;

    // Contact Information
    private String primaryPhone;
    private String secondaryPhone;
    private String emailId;
    private String alternateEmail;

    // Address
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private Double latitude;
    private Double longitude;

    // Key Person / Owner
    private String ownerName;
    private String ownerPhone;
    private String ownerEmail;
    private String ownerDesignation;

    // Financial Information
    private String bankName;
    private String bankBranch;
    private BigDecimal creditLimit;
    private String paymentTerms;

    // Ratings
    private Double rating;
    private Integer totalReviews;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
