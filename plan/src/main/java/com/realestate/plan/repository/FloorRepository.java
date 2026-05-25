package com.realestate.plan.repository;

import com.realestate.plan.entity.Floor;
import com.realestate.plan.enums.FloorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {

    List<Floor> findByTowerIdAndIsDeletedFalse(Long towerId);

    Optional<Floor> findByTowerIdAndFloorNumberAndIsDeletedFalse(Long towerId, Integer floorNumber);

    Optional<Floor> findByFloorCodeAndIsDeletedFalse(String floorCode);

    List<Floor> findByTowerIdAndFloorStatusAndIsDeletedFalse(Long towerId, FloorStatus status);
}
