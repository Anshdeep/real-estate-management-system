package com.realestate.plan.dto;

import com.realestate.plan.enums.BuilderStatus;
import com.realestate.plan.enums.BuilderType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuilderRequestDTO {

    // ===================== BASIC INFORMATION =====================
    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name must not exceed 200 characters")
    private String companyName;

    @Size(max = 200, message = "Brand name must not exceed 200 characters")
    private String brandName;

    @NotNull(message = "Builder type is required")
    private BuilderType type;

    @NotNull(message = "Builder status is required")
    private BuilderStatus status;

    @Min(value = 1800, message = "Established year must be valid")
    @Max(value = 2100, message = "Established year must be valid")
    private Integer establishedYear;

    private String description;

    @Size(max = 500, message = "Logo URL must not exceed 500 characters")
    private String logoUrl;

    @Size(max = 300, message = "Website URL must not exceed 300 characters")
    private String websiteUrl;

    // ===================== REGISTRATION & LEGAL =====================
    @Size(max = 100, message = "Registration number must not exceed 100 characters")
    private String registrationNumber;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
             message = "Invalid GST number format")
    private String gstNumber;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
             message = "Invalid PAN number format")
    private String panNumber;

    @Size(max = 100, message = "RERA number must not exceed 100 characters")
    private String reraNumber;

    @Size(max = 10, message = "RERA state code must not exceed 10 characters")
    private String reraStateCode;

    private LocalDate reraExpiryDate;

    @Size(max = 100, message = "License number must not exceed 100 characters")
    private String licenseNumber;

    // ===================== CONTACT INFORMATION =====================
    @NotBlank(message = "Primary phone is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid primary phone number")
    private String primaryPhone;

    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid secondary phone number")
    private String secondaryPhone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String emailId;

    @Email(message = "Invalid alternate email address")
    private String alternateEmail;

    // ===================== ADDRESS =====================
    @Size(max = 300)
    private String addressLine1;

    @Size(max = 300)
    private String addressLine2;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    @Builder.Default
    private String country = "India";

    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid pincode")
    private String pincode;

    private Double latitude;
    private Double longitude;

    // ===================== KEY PERSON / OWNER =====================
    @Size(max = 150)
    private String ownerName;

    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid owner phone number")
    private String ownerPhone;

    @Email(message = "Invalid owner email")
    private String ownerEmail;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid owner PAN")
    private String ownerPan;

    @Size(max = 50)
    private String ownerDesignation;

    // ===================== FINANCIAL INFORMATION =====================
    @Size(max = 100)
    private String bankName;

    @Size(max = 30)
    private String bankAccountNumber;

    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code")
    private String ifscCode;

    @Size(max = 100)
    private String bankBranch;

    @DecimalMin(value = "0.0", message = "Credit limit must be non-negative")
    private BigDecimal creditLimit;

    @Size(max = 100)
    private String paymentTerms;
}
