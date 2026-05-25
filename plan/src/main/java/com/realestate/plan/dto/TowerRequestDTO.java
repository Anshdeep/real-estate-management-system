package com.realestate.plan.dto;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.TowerStatus;
import com.realestate.plan.enums.TowerType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TowerRequestDTO {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotBlank(message = "Tower name is required")
    @Size(max = 150, message = "Tower name must not exceed 150 characters")
    private String towerName;

    @Size(max = 40, message = "Tower code must not exceed 40 characters")
    private String towerCode;

    @NotNull(message = "Tower type is required")
    private TowerType towerType;

    @NotNull(message = "Tower status is required")
    private TowerStatus towerStatus;

    @NotNull(message = "Lift count is required")
    @PositiveOrZero(message = "Lift count must be zero or positive")
    private Integer liftCount;

    @NotNull(message = "Total floors is required")
    @Positive(message = "Total floors must be greater than zero")
    private Integer totalFloors;

    @NotNull(message = "Floor capacity is required")
    @PositiveOrZero(message = "Floor capacity must be zero or positive")
    private Integer floorCapacity;

    @PositiveOrZero(message = "Total flats must be zero or positive")
    private Integer totalFlats;

    @PositiveOrZero(message = "Parking slots must be zero or positive")
    private Integer parkingSlots;

    @DecimalMin(value = "0.0", inclusive = false, message = "Built-up area must be positive")
    private Double builtUpArea;

    @DecimalMin(value = "0.0", inclusive = false, message = "Carpet area must be positive")
    private Double carpetArea;

    @DecimalMin(value = "0.0", inclusive = false, message = "Super built-up area must be positive")
    private Double superBuiltUpArea;

    private AreaUnit areaUnit;

    @Size(max = 300, message = "Address line 1 must not exceed 300 characters")
    private String addressLine1;

    @Size(max = 300, message = "Address line 2 must not exceed 300 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid pincode")
    private String pincode;

    private Double latitude;
    private Double longitude;

    private LocalDate startDate;
    private LocalDate expectedCompletionDate;
    private LocalDate actualCompletionDate;
}
