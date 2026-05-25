package com.realestate.plan.service.impl;

import com.realestate.plan.dto.FloorRequestDTO;
import com.realestate.plan.dto.FloorResponseDTO;
import com.realestate.plan.entity.Floor;
import com.realestate.plan.entity.Tower;
import com.realestate.plan.enums.FloorStatus;
import com.realestate.plan.exception.DuplicateResourceException;
import com.realestate.plan.exception.ResourceNotFoundException;
import com.realestate.plan.repository.FloorRepository;
import com.realestate.plan.repository.TowerRepository;
import com.realestate.plan.service.FloorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;
    private final TowerRepository towerRepository;

    public FloorServiceImpl(FloorRepository floorRepository, TowerRepository towerRepository) {
        this.floorRepository = floorRepository;
        this.towerRepository = towerRepository;
    }

    @Override
    public FloorResponseDTO createFloor(FloorRequestDTO dto) {
        log.info("Creating floor {} for tower {}", dto.getFloorNumber(), dto.getTowerId());

        // Validate tower exists
        Tower tower = towerRepository.findByIdAndIsDeletedFalse(dto.getTowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Tower not found with ID: " + dto.getTowerId()));

        // Check for duplicate floor number in same tower
        floorRepository.findByTowerIdAndFloorNumberAndIsDeletedFalse(dto.getTowerId(), dto.getFloorNumber())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Floor number " + dto.getFloorNumber() + " already exists in tower " + tower.getTowerName());
                });

        // Generate floor code
        String floorCode = "FLR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Floor floor = Floor.builder()
                .floorCode(floorCode)
                .floorNumber(dto.getFloorNumber())
                .floorType(dto.getFloorType())
                .floorStatus(dto.getFloorStatus())
                .totalFlats(dto.getTotalFlats())
                .occupiedFlats(0)
                .carpetArea(dto.getCarpetArea())
                .builtUpArea(dto.getBuiltUpArea())
                .superBuiltUpArea(dto.getSuperBuiltUpArea())
                .areaUnit(dto.getAreaUnit())
                .tower(tower)
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .pincode(dto.getPincode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .startDate(dto.getStartDate())
                .expectedCompletionDate(dto.getExpectedCompletionDate())
                .actualCompletionDate(dto.getActualCompletionDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        Floor savedFloor = floorRepository.save(floor);
        log.info("Floor created with code: {}", floorCode);

        return mapToResponseDTO(savedFloor);
    }

    @Override
    public FloorResponseDTO updateFloor(Long id, FloorRequestDTO dto) {
        log.info("Updating floor with ID: {}", id);

        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with ID: " + id));

        if (floor.getIsDeleted()) {
            throw new ResourceNotFoundException("Floor not found with ID: " + id);
        }

        // If floor number changed, check for duplicates
        if (!floor.getFloorNumber().equals(dto.getFloorNumber())) {
            floorRepository.findByTowerIdAndFloorNumberAndIsDeletedFalse(dto.getTowerId(), dto.getFloorNumber())
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException(
                                "Floor number " + dto.getFloorNumber() + " already exists in this tower");
                    });
            floor.setFloorNumber(dto.getFloorNumber());
        }

        floor.setFloorType(dto.getFloorType());
        floor.setFloorStatus(dto.getFloorStatus());
        floor.setTotalFlats(dto.getTotalFlats());
        floor.setCarpetArea(dto.getCarpetArea());
        floor.setBuiltUpArea(dto.getBuiltUpArea());
        floor.setSuperBuiltUpArea(dto.getSuperBuiltUpArea());
        floor.setAreaUnit(dto.getAreaUnit());
        floor.setAddressLine1(dto.getAddressLine1());
        floor.setAddressLine2(dto.getAddressLine2());
        floor.setCity(dto.getCity());
        floor.setState(dto.getState());
        floor.setCountry(dto.getCountry());
        floor.setPincode(dto.getPincode());
        floor.setLatitude(dto.getLatitude());
        floor.setLongitude(dto.getLongitude());
        floor.setStartDate(dto.getStartDate());
        floor.setExpectedCompletionDate(dto.getExpectedCompletionDate());
        floor.setActualCompletionDate(dto.getActualCompletionDate());
        floor.setUpdatedAt(LocalDateTime.now());

        Floor updatedFloor = floorRepository.save(floor);
        log.info("Floor updated with ID: {}", id);

        return mapToResponseDTO(updatedFloor);
    }

    @Override
    public void deleteFloor(Long id) {
        log.info("Deleting floor with ID: {}", id);

        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with ID: " + id));

        if (floor.getIsDeleted()) {
            throw new ResourceNotFoundException("Floor not found with ID: " + id);
        }

        floor.setIsDeleted(true);
        floor.setUpdatedAt(LocalDateTime.now());
        floorRepository.save(floor);

        log.info("Floor deleted (soft delete) with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public FloorResponseDTO getFloorById(Long id) {
        log.info("Fetching floor with ID: {}", id);

        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with ID: " + id));

        if (floor.getIsDeleted()) {
            throw new ResourceNotFoundException("Floor not found with ID: " + id);
        }

        return mapToResponseDTO(floor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FloorResponseDTO> getFloorsByTower(Long towerId) {
        log.info("Fetching all floors for tower: {}", towerId);

        return floorRepository.findByTowerIdAndIsDeletedFalse(towerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FloorResponseDTO> getFloorsByTowerAndStatus(Long towerId, FloorStatus status) {
        log.info("Fetching floors for tower {} with status: {}", towerId, status);

        return floorRepository.findByTowerIdAndFloorStatusAndIsDeletedFalse(towerId, status)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private FloorResponseDTO mapToResponseDTO(Floor floor) {
        return FloorResponseDTO.builder()
                .id(floor.getId())
                .floorCode(floor.getFloorCode())
                .floorNumber(floor.getFloorNumber())
                .floorType(floor.getFloorType())
                .floorStatus(floor.getFloorStatus())
                .totalFlats(floor.getTotalFlats())
                .occupiedFlats(floor.getOccupiedFlats())
                .carpetArea(floor.getCarpetArea())
                .builtUpArea(floor.getBuiltUpArea())
                .superBuiltUpArea(floor.getSuperBuiltUpArea())
                .areaUnit(floor.getAreaUnit())
                .towerId(floor.getTower().getId())
                .towerCode(floor.getTower().getTowerCode())
                .towerName(floor.getTower().getTowerName())
                .addressLine1(floor.getAddressLine1())
                .addressLine2(floor.getAddressLine2())
                .city(floor.getCity())
                .state(floor.getState())
                .country(floor.getCountry())
                .pincode(floor.getPincode())
                .latitude(floor.getLatitude())
                .longitude(floor.getLongitude())
                .startDate(floor.getStartDate())
                .expectedCompletionDate(floor.getExpectedCompletionDate())
                .actualCompletionDate(floor.getActualCompletionDate())
                .createdAt(floor.getCreatedAt())
                .updatedAt(floor.getUpdatedAt())
                .build();
    }
}
