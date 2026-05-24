package com.realestate.plan.controller;

import com.realestate.plan.dto.BuilderRequestDTO;
import com.realestate.plan.dto.BuilderResponseDTO;
import com.realestate.plan.enums.BuilderStatus;
import com.realestate.plan.service.BuilderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/builders")
@RequiredArgsConstructor
@Slf4j
public class BuilderController {

    private final BuilderService builderService;

    // ===================== CREATE =====================

    /**
     * POST /api/v1/builders
     * Register a new builder
     */
    @PostMapping
    public ResponseEntity<BuilderResponseDTO> createBuilder(
            @Valid @RequestBody BuilderRequestDTO requestDTO) {
        log.info("REST request to create builder: {}", requestDTO.getCompanyName());
        BuilderResponseDTO response = builderService.createBuilder(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ===================== READ =====================

    /**
     * GET /api/v1/builders
     * Get all builders
     */
    @GetMapping
    public ResponseEntity<List<BuilderResponseDTO>> getAllBuilders() {
        log.info("REST request to get all builders");
        return ResponseEntity.ok(builderService.getAllBuilders());
    }

    /**
     * GET /api/v1/builders/{id}
     * Get builder by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BuilderResponseDTO> getBuilderById(@PathVariable Long id) {
        log.info("REST request to get builder by ID: {}", id);
        return ResponseEntity.ok(builderService.getBuilderById(id));
    }

    /**
     * GET /api/v1/builders/code/{builderCode}
     * Get builder by builder code
     */
    @GetMapping("/code/{builderCode}")
    public ResponseEntity<BuilderResponseDTO> getBuilderByCode(
            @PathVariable String builderCode) {
        log.info("REST request to get builder by code: {}", builderCode);
        return ResponseEntity.ok(builderService.getBuilderByCode(builderCode));
    }

    /**
     * GET /api/v1/builders/status/{status}
     * Get builders by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BuilderResponseDTO>> getBuildersByStatus(
            @PathVariable BuilderStatus status) {
        log.info("REST request to get builders by status: {}", status);
        return ResponseEntity.ok(builderService.getBuildersByStatus(status));
    }

    /**
     * GET /api/v1/builders/city/{city}
     * Get builders by city
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<BuilderResponseDTO>> getBuildersByCity(
            @PathVariable String city) {
        log.info("REST request to get builders by city: {}", city);
        return ResponseEntity.ok(builderService.getBuildersByCity(city));
    }

    /**
     * GET /api/v1/builders/state/{state}
     * Get builders by state
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<List<BuilderResponseDTO>> getBuildersByState(
            @PathVariable String state) {
        log.info("REST request to get builders by state: {}", state);
        return ResponseEntity.ok(builderService.getBuildersByState(state));
    }

    /**
     * GET /api/v1/builders/search?keyword=xyz
     * Search builders by keyword (company name, brand name, city)
     */
    @GetMapping("/search")
    public ResponseEntity<List<BuilderResponseDTO>> searchBuilders(
            @RequestParam String keyword) {
        log.info("REST request to search builders with keyword: {}", keyword);
        return ResponseEntity.ok(builderService.searchBuilders(keyword));
    }

    /**
     * GET /api/v1/builders/count/active
     * Get count of active builders
     */
    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveBuilders() {
        return ResponseEntity.ok(builderService.countActiveBuilders());
    }

    // ===================== UPDATE =====================

    /**
     * PUT /api/v1/builders/{id}
     * Update full builder details
     */
    @PutMapping("/{id}")
    public ResponseEntity<BuilderResponseDTO> updateBuilder(
            @PathVariable Long id,
            @Valid @RequestBody BuilderRequestDTO requestDTO) {
        log.info("REST request to update builder with ID: {}", id);
        return ResponseEntity.ok(builderService.updateBuilder(id, requestDTO));
    }

    /**
     * PATCH /api/v1/builders/{id}/status
     * Update only the builder status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<BuilderResponseDTO> updateBuilderStatus(
            @PathVariable Long id,
            @RequestParam BuilderStatus status) {
        log.info("REST request to update builder status to {} for ID: {}", status, id);
        return ResponseEntity.ok(builderService.updateBuilderStatus(id, status));
    }

    // ===================== DELETE =====================

    /**
     * DELETE /api/v1/builders/{id}
     * Soft delete a builder
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilder(@PathVariable Long id) {
        log.info("REST request to delete builder with ID: {}", id);
        builderService.deleteBuilder(id);
        return ResponseEntity.noContent().build();
    }
}
