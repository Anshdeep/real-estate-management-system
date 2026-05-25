package com.realestate.plan.service;

import com.realestate.plan.dto.FlatRequestDTO;
import com.realestate.plan.dto.FlatResponseDTO;
import com.realestate.plan.enums.FlatStatus;

import java.util.List;

public interface FlatService {

    FlatResponseDTO createFlat(FlatRequestDTO dto);

    FlatResponseDTO updateFlat(Long id, FlatRequestDTO dto);

    void deleteFlat(Long id);

    FlatResponseDTO getFlatById(Long id);

    List<FlatResponseDTO> getFlatsByFloor(Long floorId);

    List<FlatResponseDTO> getFlatsByFloorAndStatus(Long floorId, FlatStatus status);
}
