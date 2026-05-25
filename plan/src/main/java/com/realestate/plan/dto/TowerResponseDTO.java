package com.realestate.plan.dto;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.TowerStatus;
import com.realestate.plan.enums.TowerType;
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
public class TowerResponseDTO {

    private Long id;
    private String towerCode;
    private String towerName;
    private Long projectId;
    private String projectName;
    private Long builderId;
    private String builderCode;
    private String builderName;
    private TowerType towerType;
    private TowerStatus towerStatus;
    private Integer liftCount;
    private Integer totalFloors;
    private Integer floorCapacity;
    private Integer totalFlats;
    private Integer parkingSlots;
    private Double builtUpArea;
    private Double carpetArea;
    private Double superBuiltUpArea;
    private AreaUnit areaUnit;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private Double latitude;
    private Double longitude;
    private LocalDate startDate;
    private LocalDate expectedCompletionDate;
    private LocalDate actualCompletionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
