package com.realestate.plan.dto;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.FloorStatus;
import com.realestate.plan.enums.FloorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloorResponseDTO {

    private Long id;
    private String floorCode;
    private Integer floorNumber;
    private FloorType floorType;
    private FloorStatus floorStatus;
    private Integer totalFlats;
    private Integer occupiedFlats;
    private Double carpetArea;
    private Double builtUpArea;
    private Double superBuiltUpArea;
    private AreaUnit areaUnit;
    private Long towerId;
    private String towerCode;
    private String towerName;
    private Double latitude;
    private Double longitude;
    private LocalDate startDate;
    private LocalDate expectedCompletionDate;
    private LocalDate actualCompletionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
