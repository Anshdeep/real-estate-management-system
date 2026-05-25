package com.realestate.plan.controller;

import com.realestate.plan.dto.FlatRequestDTO;
import com.realestate.plan.dto.FlatResponseDTO;
import com.realestate.plan.enums.FlatStatus;
import com.realestate.plan.service.FlatService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flats")
@Slf4j
public class FlatController {

    private final FlatService flatService;

    public FlatController(FlatService flatService) {
        this.flatService = flatService;
    }

    @PostMapping
    public ResponseEntity<FlatResponseDTO> createFlat(@Valid @RequestBody FlatRequestDTO dto) {
        log.info("REST request to create flat: {}", dto.getFlatNumber());
        FlatResponseDTO response = flatService.createFlat(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlatResponseDTO> updateFlat(@PathVariable Long id, @Valid @RequestBody FlatRequestDTO dto) {
        log.info("REST request to update flat: {}", id);
        FlatResponseDTO response = flatService.updateFlat(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlat(@PathVariable Long id) {
        log.info("REST request to delete flat: {}", id);
        flatService.deleteFlat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlatResponseDTO> getFlat(@PathVariable Long id) {
        log.info("REST request to get flat: {}", id);
        FlatResponseDTO response = flatService.getFlatById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/floor/{floorId}")
    public ResponseEntity<List<FlatResponseDTO>> getFlatsByFloor(@PathVariable Long floorId) {
        log.info("REST request to get flats for floor: {}", floorId);
        List<FlatResponseDTO> response = flatService.getFlatsByFloor(floorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/floor/{floorId}/status/{status}")
    public ResponseEntity<List<FlatResponseDTO>> getFlatsByFloorAndStatus(@PathVariable Long floorId, @PathVariable FlatStatus status) {
        log.info("REST request to get flats for floor {} with status: {}", floorId, status);
        List<FlatResponseDTO> response = flatService.getFlatsByFloorAndStatus(floorId, status);
        return ResponseEntity.ok(response);
    }
}
