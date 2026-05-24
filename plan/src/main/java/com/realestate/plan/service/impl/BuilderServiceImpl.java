package com.realestate.plan.service.impl;

import com.realestate.plan.dto.BuilderRequestDTO;
import com.realestate.plan.dto.BuilderResponseDTO;
import com.realestate.plan.entity.Builder;
import com.realestate.plan.enums.BuilderStatus;
import com.realestate.plan.repository.BuilderRepository;
import com.realestate.plan.service.BuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BuilderServiceImpl implements BuilderService {

    private final BuilderRepository builderRepository;

    // ===================== CREATE =====================
    @Override
    public BuilderResponseDTO createBuilder(BuilderRequestDTO requestDTO) {
        log.info("Creating builder with company name: {}", requestDTO.getCompanyName());

        if (builderRepository.existsByEmailId(requestDTO.getEmailId())) {
            throw new IllegalArgumentException("Builder with email already exists: " + requestDTO.getEmailId());
        }
        if (requestDTO.getGstNumber() != null && builderRepository.existsByGstNumber(requestDTO.getGstNumber())) {
            throw new IllegalArgumentException("Builder with GST number already exists: " + requestDTO.getGstNumber());
        }
        if (requestDTO.getPanNumber() != null && builderRepository.existsByPanNumber(requestDTO.getPanNumber())) {
            throw new IllegalArgumentException("Builder with PAN number already exists: " + requestDTO.getPanNumber());
        }
        if (requestDTO.getReraNumber() != null && builderRepository.existsByReraNumber(requestDTO.getReraNumber())) {
            throw new IllegalArgumentException("Builder with RERA number already exists: " + requestDTO.getReraNumber());
        }

        Builder builder = mapToEntity(requestDTO);
        builder.setBuilderCode(generateBuilderCode());

        Builder saved = builderRepository.save(builder);
        log.info("Builder created with code: {}", saved.getBuilderCode());
        return mapToResponseDTO(saved);
    }

    // ===================== READ =====================
    @Override
    @Transactional(readOnly = true)
    public BuilderResponseDTO getBuilderById(Long id) {
        log.info("Fetching builder by ID: {}", id);
        Builder builder = builderRepository.findById(id)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new NoSuchElementException("Builder not found with ID: " + id));
        return mapToResponseDTO(builder);
    }

    @Override
    @Transactional(readOnly = true)
    public BuilderResponseDTO getBuilderByCode(String builderCode) {
        log.info("Fetching builder by code: {}", builderCode);
        Builder builder = builderRepository.findByBuilderCodeAndIsDeletedFalse(builderCode)
                .orElseThrow(() -> new NoSuchElementException("Builder not found with code: " + builderCode));
        return mapToResponseDTO(builder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuilderResponseDTO> getAllBuilders() {
        log.info("Fetching all builders");
        return builderRepository.findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuilderResponseDTO> getBuildersByStatus(BuilderStatus status) {
        log.info("Fetching builders by status: {}", status);
        return builderRepository.findByStatusAndIsDeletedFalse(status)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuilderResponseDTO> getBuildersByCity(String city) {
        log.info("Fetching builders by city: {}", city);
        return builderRepository.findByCityAndIsDeletedFalse(city)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuilderResponseDTO> getBuildersByState(String state) {
        log.info("Fetching builders by state: {}", state);
        return builderRepository.findByStateAndIsDeletedFalse(state)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuilderResponseDTO> searchBuilders(String keyword) {
        log.info("Searching builders with keyword: {}", keyword);
        return builderRepository.searchBuilders(keyword)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ===================== UPDATE =====================
    @Override
    public BuilderResponseDTO updateBuilder(Long id, BuilderRequestDTO requestDTO) {
        log.info("Updating builder with ID: {}", id);
        Builder builder = builderRepository.findById(id)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new NoSuchElementException("Builder not found with ID: " + id));

        // Check unique constraints on email if changed
        if (!builder.getEmailId().equals(requestDTO.getEmailId()) &&
                builderRepository.existsByEmailId(requestDTO.getEmailId())) {
            throw new IllegalArgumentException("Email already in use: " + requestDTO.getEmailId());
        }

        updateEntityFromDTO(builder, requestDTO);
        Builder updated = builderRepository.save(builder);
        log.info("Builder updated: {}", updated.getBuilderCode());
        return mapToResponseDTO(updated);
    }

    @Override
    public BuilderResponseDTO updateBuilderStatus(Long id, BuilderStatus status) {
        log.info("Updating builder status to {} for ID: {}", status, id);
        Builder builder = builderRepository.findById(id)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new NoSuchElementException("Builder not found with ID: " + id));
        builder.setStatus(status);
        return mapToResponseDTO(builderRepository.save(builder));
    }

    // ===================== DELETE =====================
    @Override
    public void deleteBuilder(Long id) {
        log.info("Soft deleting builder with ID: {}", id);
        Builder builder = builderRepository.findById(id)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new NoSuchElementException("Builder not found with ID: " + id));
        builder.setIsDeleted(true);
        builderRepository.save(builder);
        log.info("Builder soft deleted: {}", builder.getBuilderCode());
    }

    // ===================== COUNT =====================
    @Override
    @Transactional(readOnly = true)
    public long countActiveBuilders() {
        return builderRepository.countActiveBuilders();
    }

    // ===================== PRIVATE HELPERS =====================
    private String generateBuilderCode() {
        String code;
        do {
            code = "BLD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (builderRepository.existsByBuilderCode(code));
        return code;
    }

    private Builder mapToEntity(BuilderRequestDTO dto) {
        return Builder.builder()
                .companyName(dto.getCompanyName())
                .brandName(dto.getBrandName())
                .type(dto.getType())
                .status(dto.getStatus() != null ? dto.getStatus() : BuilderStatus.ACTIVE)
                .establishedYear(dto.getEstablishedYear())
                .description(dto.getDescription())
                .logoUrl(dto.getLogoUrl())
                .websiteUrl(dto.getWebsiteUrl())
                .registrationNumber(dto.getRegistrationNumber())
                .gstNumber(dto.getGstNumber())
                .panNumber(dto.getPanNumber())
                .reraNumber(dto.getReraNumber())
                .reraStateCode(dto.getReraStateCode())
                .reraExpiryDate(dto.getReraExpiryDate())
                .licenseNumber(dto.getLicenseNumber())
                .primaryPhone(dto.getPrimaryPhone())
                .secondaryPhone(dto.getSecondaryPhone())
                .emailId(dto.getEmailId())
                .alternateEmail(dto.getAlternateEmail())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry() != null ? dto.getCountry() : "India")
                .pincode(dto.getPincode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .ownerName(dto.getOwnerName())
                .ownerPhone(dto.getOwnerPhone())
                .ownerEmail(dto.getOwnerEmail())
                .ownerPan(dto.getOwnerPan())
                .ownerDesignation(dto.getOwnerDesignation())
                .bankName(dto.getBankName())
                .bankAccountNumber(dto.getBankAccountNumber())
                .ifscCode(dto.getIfscCode())
                .bankBranch(dto.getBankBranch())
                .creditLimit(dto.getCreditLimit())
                .paymentTerms(dto.getPaymentTerms())
                .build();
    }

    private void updateEntityFromDTO(Builder builder, BuilderRequestDTO dto) {
        builder.setCompanyName(dto.getCompanyName());
        builder.setBrandName(dto.getBrandName());
        builder.setType(dto.getType());
        builder.setStatus(dto.getStatus());
        builder.setEstablishedYear(dto.getEstablishedYear());
        builder.setDescription(dto.getDescription());
        builder.setLogoUrl(dto.getLogoUrl());
        builder.setWebsiteUrl(dto.getWebsiteUrl());
        builder.setRegistrationNumber(dto.getRegistrationNumber());
        builder.setGstNumber(dto.getGstNumber());
        builder.setPanNumber(dto.getPanNumber());
        builder.setReraNumber(dto.getReraNumber());
        builder.setReraStateCode(dto.getReraStateCode());
        builder.setReraExpiryDate(dto.getReraExpiryDate());
        builder.setLicenseNumber(dto.getLicenseNumber());
        builder.setPrimaryPhone(dto.getPrimaryPhone());
        builder.setSecondaryPhone(dto.getSecondaryPhone());
        builder.setEmailId(dto.getEmailId());
        builder.setAlternateEmail(dto.getAlternateEmail());
        builder.setAddressLine1(dto.getAddressLine1());
        builder.setAddressLine2(dto.getAddressLine2());
        builder.setCity(dto.getCity());
        builder.setState(dto.getState());
        builder.setCountry(dto.getCountry());
        builder.setPincode(dto.getPincode());
        builder.setLatitude(dto.getLatitude());
        builder.setLongitude(dto.getLongitude());
        builder.setOwnerName(dto.getOwnerName());
        builder.setOwnerPhone(dto.getOwnerPhone());
        builder.setOwnerEmail(dto.getOwnerEmail());
        builder.setOwnerPan(dto.getOwnerPan());
        builder.setOwnerDesignation(dto.getOwnerDesignation());
        builder.setBankName(dto.getBankName());
        builder.setBankAccountNumber(dto.getBankAccountNumber());
        builder.setIfscCode(dto.getIfscCode());
        builder.setBankBranch(dto.getBankBranch());
        builder.setCreditLimit(dto.getCreditLimit());
        builder.setPaymentTerms(dto.getPaymentTerms());
    }

    private BuilderResponseDTO mapToResponseDTO(Builder builder) {
        return BuilderResponseDTO.builder()
                .id(builder.getId())
                .builderCode(builder.getBuilderCode())
                .companyName(builder.getCompanyName())
                .brandName(builder.getBrandName())
                .type(builder.getType())
                .status(builder.getStatus())
                .establishedYear(builder.getEstablishedYear())
                .description(builder.getDescription())
                .logoUrl(builder.getLogoUrl())
                .websiteUrl(builder.getWebsiteUrl())
                .registrationNumber(builder.getRegistrationNumber())
                .gstNumber(builder.getGstNumber())
                .panNumber(builder.getPanNumber())
                .reraNumber(builder.getReraNumber())
                .reraStateCode(builder.getReraStateCode())
                .reraExpiryDate(builder.getReraExpiryDate())
                .licenseNumber(builder.getLicenseNumber())
                .primaryPhone(builder.getPrimaryPhone())
                .secondaryPhone(builder.getSecondaryPhone())
                .emailId(builder.getEmailId())
                .alternateEmail(builder.getAlternateEmail())
                .addressLine1(builder.getAddressLine1())
                .addressLine2(builder.getAddressLine2())
                .city(builder.getCity())
                .state(builder.getState())
                .country(builder.getCountry())
                .pincode(builder.getPincode())
                .latitude(builder.getLatitude())
                .longitude(builder.getLongitude())
                .ownerName(builder.getOwnerName())
                .ownerPhone(builder.getOwnerPhone())
                .ownerEmail(builder.getOwnerEmail())
                .ownerDesignation(builder.getOwnerDesignation())
                .bankName(builder.getBankName())
                .bankBranch(builder.getBankBranch())
                .creditLimit(builder.getCreditLimit())
                .paymentTerms(builder.getPaymentTerms())
                .rating(builder.getRating())
                .totalReviews(builder.getTotalReviews())
                .createdAt(builder.getCreatedAt())
                .updatedAt(builder.getUpdatedAt())
                .createdBy(builder.getCreatedBy())
                .updatedBy(builder.getUpdatedBy())
                .build();
    }
}
