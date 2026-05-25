package com.realestate.plan.repository;

import com.realestate.plan.entity.Tower;
import com.realestate.plan.enums.TowerStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TowerRepository extends JpaRepository<Tower, Long> {

    @EntityGraph(attributePaths = "project")
    Optional<Tower> findByIdAndIsDeletedFalse(Long id);

    @EntityGraph(attributePaths = "project")
    List<Tower> findByProjectIdAndIsDeletedFalse(Long projectId);

    List<Tower> findByProjectBuilderIdAndIsDeletedFalse(Long builderId);

    List<Tower> findByTowerStatusAndIsDeletedFalse(TowerStatus towerStatus);

    boolean existsByProjectIdAndTowerNameAndIsDeletedFalse(Long projectId, String towerName);

    boolean existsByProjectIdAndTowerCodeAndIsDeletedFalse(Long projectId, String towerCode);

    boolean existsByTowerCodeAndIsDeletedFalse(String towerCode);

    boolean existsByProjectIdAndTowerNameAndIsDeletedFalseAndIdNot(Long projectId, String towerName, Long id);

    boolean existsByProjectIdAndTowerCodeAndIsDeletedFalseAndIdNot(Long projectId, String towerCode, Long id);
}
