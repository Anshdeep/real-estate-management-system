package com.realestate.plan.service;

import com.realestate.plan.dto.ProjectRequestDTO;
import com.realestate.plan.dto.ProjectResponseDTO;
import com.realestate.plan.enums.ProjectStatus;

import java.util.List;

public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO requestDTO);

    ProjectResponseDTO getProjectById(Long id);

    ProjectResponseDTO getProjectByCode(String projectCode);

    List<ProjectResponseDTO> getAllProjects();

    List<ProjectResponseDTO> getProjectsByStatus(ProjectStatus status);

    List<ProjectResponseDTO> getProjectsByBuilderId(Long builderId);

    List<ProjectResponseDTO> searchProjects(String keyword);

    ProjectResponseDTO updateProject(Long id, ProjectRequestDTO requestDTO);

    ProjectResponseDTO updateProjectStatus(Long id, ProjectStatus status);

    void deleteProject(Long id);
}
