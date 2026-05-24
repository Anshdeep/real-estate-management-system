package com.realestate.plan.controller;

import com.realestate.plan.dto.ProjectRequestDTO;
import com.realestate.plan.dto.ProjectResponseDTO;
import com.realestate.plan.enums.ProjectStatus;
import com.realestate.plan.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectRequestDTO requestDTO) {
        log.info("REST request to create project: {}", requestDTO.getProjectName());
        ProjectResponseDTO response = projectService.createProject(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        log.info("REST request to get all projects");
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        log.info("REST request to get project by ID: {}", id);
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/code/{projectCode}")
    public ResponseEntity<ProjectResponseDTO> getProjectByCode(@PathVariable String projectCode) {
        log.info("REST request to get project by code: {}", projectCode);
        return ResponseEntity.ok(projectService.getProjectByCode(projectCode));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByStatus(@PathVariable ProjectStatus status) {
        log.info("REST request to get projects by status: {}", status);
        return ResponseEntity.ok(projectService.getProjectsByStatus(status));
    }

    @GetMapping("/builder/{builderId}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByBuilderId(@PathVariable Long builderId) {
        log.info("REST request to get projects by builder ID: {}", builderId);
        return ResponseEntity.ok(projectService.getProjectsByBuilderId(builderId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponseDTO>> searchProjects(@RequestParam String keyword) {
        log.info("REST request to search projects with keyword: {}", keyword);
        return ResponseEntity.ok(projectService.searchProjects(keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDTO requestDTO) {
        log.info("REST request to update project ID: {}", id);
        return ResponseEntity.ok(projectService.updateProject(id, requestDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectResponseDTO> updateProjectStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {
        log.info("REST request to update project status to {} for ID: {}", status, id);
        return ResponseEntity.ok(projectService.updateProjectStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.info("REST request to delete project ID: {}", id);
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
