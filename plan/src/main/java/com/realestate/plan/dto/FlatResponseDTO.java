package com.realestate.plan.dto;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.BHKType;
import com.realestate.plan.enums.FlatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlatResponseDTO {

    private Long id;
    private String flatCode;
    private String flatNumber;
    private BHKType bhkType;
    private FlatStatus flatStatus;
    private Double builtUpArea;
    private Double superBuiltUpArea;
    private Double carpetArea;
    private AreaUnit areaUnit;

    private Long floorId;
    private Integer floorNumber;
    private Long towerId;
    private String towerCode;
    private Long projectId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
