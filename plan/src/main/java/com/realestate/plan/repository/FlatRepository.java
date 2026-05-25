package com.realestate.plan.repository;

import com.realestate.plan.entity.Flat;
import com.realestate.plan.enums.FlatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {

    List<Flat> findByFloorIdAndIsDeletedFalse(Long floorId);

    Optional<Flat> findByFloorIdAndFlatNumberAndIsDeletedFalse(Long floorId, String flatNumber);

    Optional<Flat> findByFlatCodeAndIsDeletedFalse(String flatCode);

    List<Flat> findByFloorIdAndFlatStatusAndIsDeletedFalse(Long floorId, FlatStatus status);
}
