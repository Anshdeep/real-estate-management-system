package com.realestate.plan.controller;

import com.realestate.plan.dto.TowerRequestDTO;
import com.realestate.plan.dto.TowerResponseDTO;
import com.realestate.plan.enums.TowerStatus;
import com.realestate.plan.service.TowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/towers")
@RequiredArgsConstructor
@Slf4j
public class TowerController {

    private final TowerService towerService;

    @PostMapping
    public ResponseEntity<TowerResponseDTO> createTower(@Valid @RequestBody TowerRequestDTO requestDTO) {
        log.info("REST request to create tower: {}", requestDTO.getTowerName());
        TowerResponseDTO response = towerService.createTower(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TowerResponseDTO>> getAllTowers() {
        log.info("REST request to get all towers");
        return ResponseEntity.ok(towerService.getAllTowers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TowerResponseDTO> getTowerById(@PathVariable Long id) {
        log.info("REST request to get tower by ID: {}", id);
        return ResponseEntity.ok(towerService.getTowerById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TowerResponseDTO>> getTowersByProject(@PathVariable Long projectId) {
        log.info("REST request to get towers by project ID: {}", projectId);
        return ResponseEntity.ok(towerService.getTowersByProjectId(projectId));
    }

    @GetMapping("/builder/{builderId}")
    public ResponseEntity<List<TowerResponseDTO>> getTowersByBuilder(@PathVariable Long builderId) {
        log.info("REST request to get towers by builder ID: {}", builderId);
        return ResponseEntity.ok(towerService.getTowersByBuilderId(builderId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TowerResponseDTO>> getTowersByStatus(@PathVariable TowerStatus status) {
        log.info("REST request to get towers by status: {}", status);
        return ResponseEntity.ok(towerService.getTowersByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TowerResponseDTO> updateTower(
            @PathVariable Long id,
            @Valid @RequestBody TowerRequestDTO requestDTO) {
        log.info("REST request to update tower ID: {}", id);
        return ResponseEntity.ok(towerService.updateTower(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTower(@PathVariable Long id) {
        log.info("REST request to delete tower ID: {}", id);
        towerService.deleteTower(id);
        return ResponseEntity.noContent().build();
    }
}
