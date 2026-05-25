package com.realestate.plan.service.impl;

import com.realestate.plan.dto.TowerRequestDTO;
import com.realestate.plan.dto.TowerResponseDTO;
import com.realestate.plan.entity.Project;
import com.realestate.plan.entity.Tower;
import com.realestate.plan.enums.TowerStatus;
import com.realestate.plan.exception.DuplicateResourceException;
import com.realestate.plan.exception.ResourceNotFoundException;
import com.realestate.plan.repository.ProjectRepository;
import com.realestate.plan.repository.TowerRepository;
import com.realestate.plan.service.TowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TowerServiceImpl implements TowerService {

    private final TowerRepository towerRepository;
    private final ProjectRepository projectRepository;

    @Override
    public TowerResponseDTO createTower(TowerRequestDTO requestDTO) {
        log.info("Creating tower {} for project {}", requestDTO.getTowerName(), requestDTO.getProjectId());

        Project project = projectRepository.findByIdAndIsDeletedFalse(requestDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + requestDTO.getProjectId()));

        if (towerRepository.existsByProjectIdAndTowerNameAndIsDeletedFalse(project.getId(), requestDTO.getTowerName())) {
            throw new DuplicateResourceException("A tower with name '" + requestDTO.getTowerName() + "' already exists for project ID " + project.getId());
        }

        if (requestDTO.getTowerCode() != null && towerRepository.existsByProjectIdAndTowerCodeAndIsDeletedFalse(project.getId(), requestDTO.getTowerCode())) {
            throw new DuplicateResourceException("A tower with code '" + requestDTO.getTowerCode() + "' already exists for project ID " + project.getId());
        }

        Tower tower = mapToEntity(requestDTO);
        tower.setTowerCode(requestDTO.getTowerCode() != null ? requestDTO.getTowerCode() : generateTowerCode());
        tower.setProject(project);

        Tower saved = towerRepository.save(tower);
        log.info("Tower created with code: {}", saved.getTowerCode());
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TowerResponseDTO getTowerById(Long id) {
        Tower tower = towerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tower not found with ID: " + id));
        return mapToResponseDTO(tower);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TowerResponseDTO> getAllTowers() {
        return towerRepository.findAll()
                .stream()
                .filter(t -> !Boolean.TRUE.equals(t.getIsDeleted()))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TowerResponseDTO> getTowersByProjectId(Long projectId) {
        return towerRepository.findByProjectIdAndIsDeletedFalse(projectId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TowerResponseDTO> getTowersByBuilderId(Long builderId) {
        return towerRepository.findByProjectBuilderIdAndIsDeletedFalse(builderId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TowerResponseDTO> getTowersByStatus(TowerStatus status) {
        return towerRepository.findByTowerStatusAndIsDeletedFalse(status)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TowerResponseDTO updateTower(Long id, TowerRequestDTO requestDTO) {
        log.info("Updating tower {}", id);
        Tower tower = towerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tower not found with ID: " + id));

        Project project = projectRepository.findByIdAndIsDeletedFalse(requestDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + requestDTO.getProjectId()));

        if (towerRepository.existsByProjectIdAndTowerNameAndIsDeletedFalseAndIdNot(project.getId(), requestDTO.getTowerName(), id)) {
            throw new DuplicateResourceException("A tower with name '" + requestDTO.getTowerName() + "' already exists for project ID " + project.getId());
        }

        if (requestDTO.getTowerCode() != null && towerRepository.existsByProjectIdAndTowerCodeAndIsDeletedFalseAndIdNot(project.getId(), requestDTO.getTowerCode(), id)) {
            throw new DuplicateResourceException("A tower with code '" + requestDTO.getTowerCode() + "' already exists for project ID " + project.getId());
        }

        updateEntityFromDTO(tower, requestDTO);
        tower.setProject(project);

        return mapToResponseDTO(towerRepository.save(tower));
    }

    @Override
    public void deleteTower(Long id) {
        Tower tower = towerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tower not found with ID: " + id));
        tower.setIsDeleted(true);
        towerRepository.save(tower);
    }

    private String generateTowerCode() {
        String code;
        do {
            code = "TWR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (towerRepository.existsByTowerCodeAndIsDeletedFalse(code));
        return code;
    }

    private Tower mapToEntity(TowerRequestDTO dto) {
        return Tower.builder()
                .towerName(dto.getTowerName())
                .towerType(dto.getTowerType())
                .towerStatus(dto.getTowerStatus())
                .liftCount(dto.getLiftCount())
                .totalFloors(dto.getTotalFloors())
                .floorCapacity(dto.getFloorCapacity())
                .totalFlats(dto.getTotalFlats())
                .parkingSlots(dto.getParkingSlots())
                .builtUpArea(dto.getBuiltUpArea())
                .carpetArea(dto.getCarpetArea())
                .superBuiltUpArea(dto.getSuperBuiltUpArea())
                .areaUnit(dto.getAreaUnit())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry() != null ? dto.getCountry() : "India")
                .pincode(dto.getPincode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .startDate(dto.getStartDate())
                .expectedCompletionDate(dto.getExpectedCompletionDate())
                .actualCompletionDate(dto.getActualCompletionDate())
                .build();
    }

    private void updateEntityFromDTO(Tower tower, TowerRequestDTO dto) {
        tower.setTowerName(dto.getTowerName());
        if (dto.getTowerCode() != null) {
            tower.setTowerCode(dto.getTowerCode());
        }
        tower.setTowerType(dto.getTowerType());
        tower.setTowerStatus(dto.getTowerStatus());
        tower.setLiftCount(dto.getLiftCount());
        tower.setTotalFloors(dto.getTotalFloors());
        tower.setFloorCapacity(dto.getFloorCapacity());
        tower.setTotalFlats(dto.getTotalFlats());
        tower.setParkingSlots(dto.getParkingSlots());
        tower.setBuiltUpArea(dto.getBuiltUpArea());
        tower.setCarpetArea(dto.getCarpetArea());
        tower.setSuperBuiltUpArea(dto.getSuperBuiltUpArea());
        tower.setAreaUnit(dto.getAreaUnit());
        tower.setAddressLine1(dto.getAddressLine1());
        tower.setAddressLine2(dto.getAddressLine2());
        tower.setCity(dto.getCity());
        tower.setState(dto.getState());
        tower.setCountry(dto.getCountry());
        tower.setPincode(dto.getPincode());
        tower.setLatitude(dto.getLatitude());
        tower.setLongitude(dto.getLongitude());
        tower.setStartDate(dto.getStartDate());
        tower.setExpectedCompletionDate(dto.getExpectedCompletionDate());
        tower.setActualCompletionDate(dto.getActualCompletionDate());
    }

    private TowerResponseDTO mapToResponseDTO(Tower tower) {
        return TowerResponseDTO.builder()
                .id(tower.getId())
                .towerCode(tower.getTowerCode())
                .towerName(tower.getTowerName())
                .projectId(tower.getProject() != null ? tower.getProject().getId() : null)
                .projectName(tower.getProject() != null ? tower.getProject().getProjectName() : null)
                .builderId(tower.getProject() != null && tower.getProject().getBuilder() != null ? tower.getProject().getBuilder().getId() : null)
                .builderCode(tower.getProject() != null && tower.getProject().getBuilder() != null ? tower.getProject().getBuilder().getBuilderCode() : null)
                .builderName(tower.getProject() != null && tower.getProject().getBuilder() != null ? tower.getProject().getBuilder().getCompanyName() : null)
                .towerType(tower.getTowerType())
                .towerStatus(tower.getTowerStatus())
                .liftCount(tower.getLiftCount())
                .totalFloors(tower.getTotalFloors())
                .floorCapacity(tower.getFloorCapacity())
                .totalFlats(tower.getTotalFlats())
                .parkingSlots(tower.getParkingSlots())
                .builtUpArea(tower.getBuiltUpArea())
                .carpetArea(tower.getCarpetArea())
                .superBuiltUpArea(tower.getSuperBuiltUpArea())
                .areaUnit(tower.getAreaUnit())
                .addressLine1(tower.getAddressLine1())
                .addressLine2(tower.getAddressLine2())
                .city(tower.getCity())
                .state(tower.getState())
                .country(tower.getCountry())
                .pincode(tower.getPincode())
                .latitude(tower.getLatitude())
                .longitude(tower.getLongitude())
                .startDate(tower.getStartDate())
                .expectedCompletionDate(tower.getExpectedCompletionDate())
                .actualCompletionDate(tower.getActualCompletionDate())
                .createdAt(tower.getCreatedAt())
                .updatedAt(tower.getUpdatedAt())
                .createdBy(tower.getCreatedBy())
                .updatedBy(tower.getUpdatedBy())
                .build();
    }
}
