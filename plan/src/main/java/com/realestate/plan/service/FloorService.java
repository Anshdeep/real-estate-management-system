package com.realestate.plan.service;

import com.realestate.plan.dto.FloorRequestDTO;
import com.realestate.plan.dto.FloorResponseDTO;
import com.realestate.plan.enums.FloorStatus;

import java.util.List;

public interface FloorService {

    FloorResponseDTO createFloor(FloorRequestDTO dto);

    FloorResponseDTO updateFloor(Long id, FloorRequestDTO dto);

    void deleteFloor(Long id);

    FloorResponseDTO getFloorById(Long id);

    List<FloorResponseDTO> getFloorsByTower(Long towerId);

    List<FloorResponseDTO> getFloorsByTowerAndStatus(Long towerId, FloorStatus status);
}
