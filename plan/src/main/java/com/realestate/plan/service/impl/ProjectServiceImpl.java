package com.realestate.plan.service.impl;

import com.realestate.plan.dto.ProjectRequestDTO;
import com.realestate.plan.dto.ProjectResponseDTO;
import com.realestate.plan.entity.Builder;
import com.realestate.plan.entity.Project;
import com.realestate.plan.enums.ProjectStatus;
import com.realestate.plan.exception.ResourceNotFoundException;
import com.realestate.plan.repository.BuilderRepository;
import com.realestate.plan.repository.ProjectRepository;
import com.realestate.plan.service.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final BuilderRepository builderRepository;

    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO requestDTO) {
        log.info("Creating project: {}", requestDTO.getProjectName());

        Builder builder = builderRepository.findById(requestDTO.getBuilderId())
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Builder not found with ID: " + requestDTO.getBuilderId()));

        Project project = mapToEntity(requestDTO);
        project.setProjectCode(generateProjectCode());
        project.setBuilder(builder);

        Project saved = projectRepository.save(project);
        log.info("Project created with code: {}", saved.getProjectCode());
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
        return mapToResponseDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectByCode(String projectCode) {
        Project project = projectRepository.findByProjectCodeAndIsDeletedFalse(projectCode)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with code: " + projectCode));
        return mapToResponseDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatusAndIsDeletedFalse(status)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getProjectsByBuilderId(Long builderId) {
        return projectRepository.findByBuilderIdAndIsDeletedFalse(builderId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> searchProjects(String keyword) {
        return projectRepository.searchProjects(keyword)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO requestDTO) {
        Project project = projectRepository.findById(id)
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        Builder builder = builderRepository.findById(requestDTO.getBuilderId())
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Builder not found with ID: " + requestDTO.getBuilderId()));

        updateEntityFromDTO(project, requestDTO);
        project.setBuilder(builder);

        return mapToResponseDTO(projectRepository.save(project));
    }

    @Override
    public ProjectResponseDTO updateProjectStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id)
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
        project.setStatus(status);
        return mapToResponseDTO(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
        project.setIsDeleted(true);
        projectRepository.save(project);
    }

    private String generateProjectCode() {
        String code;
        do {
            code = "PRJ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (projectRepository.existsByProjectCode(code));
        return code;
    }

    private Project mapToEntity(ProjectRequestDTO dto) {
        return Project.builder()
                .projectName(dto.getProjectName())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .phase(dto.getPhase())
                .totalArea(dto.getTotalArea())
                .builtUpArea(dto.getBuiltUpArea())
                .areaUnit(dto.getAreaUnit())
                .plotArea(dto.getPlotArea())
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
                .build();
    }

    private void updateEntityFromDTO(Project project, ProjectRequestDTO dto) {
        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStatus(dto.getStatus());
        project.setPhase(dto.getPhase());
        project.setTotalArea(dto.getTotalArea());
        project.setBuiltUpArea(dto.getBuiltUpArea());
        project.setAreaUnit(dto.getAreaUnit());
        project.setPlotArea(dto.getPlotArea());
        project.setAddressLine1(dto.getAddressLine1());
        project.setAddressLine2(dto.getAddressLine2());
        project.setCity(dto.getCity());
        project.setState(dto.getState());
        project.setCountry(dto.getCountry());
        project.setPincode(dto.getPincode());
        project.setLatitude(dto.getLatitude());
        project.setLongitude(dto.getLongitude());
        project.setStartDate(dto.getStartDate());
        project.setExpectedCompletionDate(dto.getExpectedCompletionDate());
        project.setActualCompletionDate(dto.getActualCompletionDate());
    }

    private ProjectResponseDTO mapToResponseDTO(Project project) {
        return ProjectResponseDTO.builder()
                .id(project.getId())
                .projectCode(project.getProjectCode())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .status(project.getStatus())
                .phase(project.getPhase())
                .builderId(project.getBuilder() != null ? project.getBuilder().getId() : null)
                .builderCode(project.getBuilder() != null ? project.getBuilder().getBuilderCode() : null)
                .builderName(project.getBuilder() != null ? project.getBuilder().getCompanyName() : null)
                .totalArea(project.getTotalArea())
                .builtUpArea(project.getBuiltUpArea())
                .areaUnit(project.getAreaUnit())
                .plotArea(project.getPlotArea())
                .addressLine1(project.getAddressLine1())
                .addressLine2(project.getAddressLine2())
                .city(project.getCity())
                .state(project.getState())
                .country(project.getCountry())
                .pincode(project.getPincode())
                .latitude(project.getLatitude())
                .longitude(project.getLongitude())
                .startDate(project.getStartDate())
                .expectedCompletionDate(project.getExpectedCompletionDate())
                .actualCompletionDate(project.getActualCompletionDate())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .createdBy(project.getCreatedBy())
                .updatedBy(project.getUpdatedBy())
                .build();
    }
}
