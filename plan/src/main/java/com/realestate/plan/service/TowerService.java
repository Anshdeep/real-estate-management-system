package com.realestate.plan.service;

import com.realestate.plan.dto.TowerRequestDTO;
import com.realestate.plan.dto.TowerResponseDTO;
import com.realestate.plan.enums.TowerStatus;

import java.util.List;

public interface TowerService {

    TowerResponseDTO createTower(TowerRequestDTO requestDTO);

    TowerResponseDTO getTowerById(Long id);

    List<TowerResponseDTO> getAllTowers();

    List<TowerResponseDTO> getTowersByProjectId(Long projectId);

    List<TowerResponseDTO> getTowersByBuilderId(Long builderId);

    List<TowerResponseDTO> getTowersByStatus(TowerStatus status);

    TowerResponseDTO updateTower(Long id, TowerRequestDTO requestDTO);

    void deleteTower(Long id);
}
