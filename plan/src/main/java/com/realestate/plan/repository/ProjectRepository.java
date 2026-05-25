package com.realestate.plan.repository;

import com.realestate.plan.entity.Project;
import com.realestate.plan.enums.ProjectStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = "builder")
    Optional<Project> findByProjectCodeAndIsDeletedFalse(String projectCode);

    @EntityGraph(attributePaths = "builder")
    Optional<Project> findByIdAndIsDeletedFalse(Long id);

    @EntityGraph(attributePaths = "builder")
    List<Project> findByStatusAndIsDeletedFalse(ProjectStatus status);

    @EntityGraph(attributePaths = "builder")
    List<Project> findByCityAndIsDeletedFalse(String city);

    @EntityGraph(attributePaths = "builder")
    List<Project> findByStateAndIsDeletedFalse(String state);

    @EntityGraph(attributePaths = "builder")
    List<Project> findByBuilderIdAndIsDeletedFalse(Long builderId);

    boolean existsByBuilderIdAndProjectNameAndIsDeletedFalse(Long builderId, String projectName);

    boolean existsByBuilderIdAndProjectNameAndIsDeletedFalseAndIdNot(Long builderId, String projectName, Long id);

    @EntityGraph(attributePaths = "builder")
    List<Project> findByIsDeletedFalse();

    boolean existsByProjectCode(String projectCode);

    @EntityGraph(attributePaths = "builder")
    @Query("SELECT p FROM Project p WHERE p.isDeleted = false AND " +
           "(LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.state) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Project> searchProjects(String keyword);
}
