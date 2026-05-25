package com.realestate.plan.service.impl;

import com.realestate.plan.dto.FlatRequestDTO;
import com.realestate.plan.dto.FlatResponseDTO;
import com.realestate.plan.entity.Flat;
import com.realestate.plan.entity.Floor;
import com.realestate.plan.enums.FlatStatus;
import com.realestate.plan.exception.DuplicateResourceException;
import com.realestate.plan.exception.ResourceNotFoundException;
import com.realestate.plan.repository.FlatRepository;
import com.realestate.plan.repository.FloorRepository;
import com.realestate.plan.service.FlatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class FlatServiceImpl implements FlatService {

    private final FlatRepository flatRepository;
    private final FloorRepository floorRepository;

    public FlatServiceImpl(FlatRepository flatRepository, FloorRepository floorRepository) {
        this.flatRepository = flatRepository;
        this.floorRepository = floorRepository;
    }

    @Override
    public FlatResponseDTO createFlat(FlatRequestDTO dto) {
        log.info("Creating flat {} for floor {}", dto.getFlatNumber(), dto.getFloorId());

        Floor floor = floorRepository.findById(dto.getFloorId())
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with ID: " + dto.getFloorId()));

        if (floor.getIsDeleted()) {
            throw new ResourceNotFoundException("Floor not found with ID: " + dto.getFloorId());
        }

        flatRepository.findByFloorIdAndFlatNumberAndIsDeletedFalse(dto.getFloorId(), dto.getFlatNumber())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Flat number " + dto.getFlatNumber() + " already exists on this floor");
                });

        String flatCode = "FLT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Flat flat = Flat.builder()
                .flatCode(flatCode)
                .flatNumber(dto.getFlatNumber())
                .bhkType(dto.getBhkType())
                .flatStatus(dto.getFlatStatus())
                .builtUpArea(dto.getBuiltUpArea())
                .superBuiltUpArea(dto.getSuperBuiltUpArea())
                .carpetArea(dto.getCarpetArea())
                .areaUnit(dto.getAreaUnit())
                .floor(floor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        Flat saved = flatRepository.save(flat);
        log.info("Flat created with code: {}", flatCode);
        return mapToResponseDTO(saved);
    }

    @Override
    public FlatResponseDTO updateFlat(Long id, FlatRequestDTO dto) {
        log.info("Updating flat with ID: {}", id);

        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found with ID: " + id));

        if (flat.getIsDeleted()) {
            throw new ResourceNotFoundException("Flat not found with ID: " + id);
        }

        if (!flat.getFlatNumber().equals(dto.getFlatNumber())) {
            flatRepository.findByFloorIdAndFlatNumberAndIsDeletedFalse(dto.getFloorId(), dto.getFlatNumber())
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException("Flat number " + dto.getFlatNumber() + " already exists on this floor");
                    });
            flat.setFlatNumber(dto.getFlatNumber());
        }

        flat.setBhkType(dto.getBhkType());
        flat.setFlatStatus(dto.getFlatStatus());
        flat.setBuiltUpArea(dto.getBuiltUpArea());
        flat.setSuperBuiltUpArea(dto.getSuperBuiltUpArea());
        flat.setCarpetArea(dto.getCarpetArea());
        flat.setAreaUnit(dto.getAreaUnit());
        flat.setUpdatedAt(LocalDateTime.now());

        Flat updated = flatRepository.save(flat);
        log.info("Flat updated with ID: {}", id);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteFlat(Long id) {
        log.info("Deleting flat with ID: {}", id);

        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found with ID: " + id));

        if (flat.getIsDeleted()) {
            throw new ResourceNotFoundException("Flat not found with ID: " + id);
        }

        flat.setIsDeleted(true);
        flat.setUpdatedAt(LocalDateTime.now());
        flatRepository.save(flat);

        log.info("Flat soft-deleted with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public FlatResponseDTO getFlatById(Long id) {
        log.info("Fetching flat with ID: {}", id);

        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found with ID: " + id));

        if (flat.getIsDeleted()) {
            throw new ResourceNotFoundException("Flat not found with ID: " + id);
        }

        return mapToResponseDTO(flat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlatResponseDTO> getFlatsByFloor(Long floorId) {
        log.info("Fetching flats for floor: {}", floorId);

        return flatRepository.findByFloorIdAndIsDeletedFalse(floorId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlatResponseDTO> getFlatsByFloorAndStatus(Long floorId, FlatStatus status) {
        log.info("Fetching flats for floor {} with status: {}", floorId, status);

        return flatRepository.findByFloorIdAndFlatStatusAndIsDeletedFalse(floorId, status)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private FlatResponseDTO mapToResponseDTO(Flat flat) {
        return FlatResponseDTO.builder()
                .id(flat.getId())
                .flatCode(flat.getFlatCode())
                .flatNumber(flat.getFlatNumber())
                .bhkType(flat.getBhkType())
                .flatStatus(flat.getFlatStatus())
                .builtUpArea(flat.getBuiltUpArea())
                .superBuiltUpArea(flat.getSuperBuiltUpArea())
                .carpetArea(flat.getCarpetArea())
                .areaUnit(flat.getAreaUnit())
                .floorId(flat.getFloor().getId())
                .floorNumber(flat.getFloor().getFloorNumber())
                .towerId(flat.getFloor().getTower().getId())
                .towerCode(flat.getFloor().getTower().getTowerCode())
                .projectId(flat.getFloor().getTower().getProject().getId())
                .createdAt(flat.getCreatedAt())
                .updatedAt(flat.getUpdatedAt())
                .build();
    }
}
