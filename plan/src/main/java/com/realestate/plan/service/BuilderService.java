package com.realestate.plan.service;

import com.realestate.plan.dto.BuilderRequestDTO;
import com.realestate.plan.dto.BuilderResponseDTO;
import com.realestate.plan.enums.BuilderStatus;

import java.util.List;

public interface BuilderService {

    BuilderResponseDTO createBuilder(BuilderRequestDTO requestDTO);

    BuilderResponseDTO getBuilderById(Long id);

    BuilderResponseDTO getBuilderByCode(String builderCode);

    List<BuilderResponseDTO> getAllBuilders();

    List<BuilderResponseDTO> getBuildersByStatus(BuilderStatus status);

    List<BuilderResponseDTO> getBuildersByCity(String city);

    List<BuilderResponseDTO> getBuildersByState(String state);

    List<BuilderResponseDTO> searchBuilders(String keyword);

    BuilderResponseDTO updateBuilder(Long id, BuilderRequestDTO requestDTO);

    BuilderResponseDTO updateBuilderStatus(Long id, BuilderStatus status);

    void deleteBuilder(Long id);

    long countActiveBuilders();
}
