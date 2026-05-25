package com.realestate.plan.controller;

import com.realestate.plan.dto.FloorRequestDTO;
import com.realestate.plan.dto.FloorResponseDTO;
import com.realestate.plan.enums.FloorStatus;
import com.realestate.plan.service.FloorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/floors")
@Slf4j
public class FloorController {

    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    @PostMapping
    public ResponseEntity<FloorResponseDTO> createFloor(@Valid @RequestBody FloorRequestDTO dto) {
        log.info("REST request to create floor: {}", dto.getFloorNumber());
        FloorResponseDTO response = floorService.createFloor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FloorResponseDTO> updateFloor(
            @PathVariable Long id,
            @Valid @RequestBody FloorRequestDTO dto) {
        log.info("REST request to update floor: {}", id);
        FloorResponseDTO response = floorService.updateFloor(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFloor(@PathVariable Long id) {
        log.info("REST request to delete floor: {}", id);
        floorService.deleteFloor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FloorResponseDTO> getFloor(@PathVariable Long id) {
        log.info("REST request to get floor: {}", id);
        FloorResponseDTO response = floorService.getFloorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tower/{towerId}")
    public ResponseEntity<List<FloorResponseDTO>> getFloorsByTower(@PathVariable Long towerId) {
        log.info("REST request to get floors for tower: {}", towerId);
        List<FloorResponseDTO> response = floorService.getFloorsByTower(towerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tower/{towerId}/status/{status}")
    public ResponseEntity<List<FloorResponseDTO>> getFloorsByTowerAndStatus(
            @PathVariable Long towerId,
            @PathVariable FloorStatus status) {
        log.info("REST request to get floors for tower {} with status: {}", towerId, status);
        List<FloorResponseDTO> response = floorService.getFloorsByTowerAndStatus(towerId, status);
        return ResponseEntity.ok(response);
    }
}
