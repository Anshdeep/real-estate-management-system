package com.realestate.plan.dto;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.FloorStatus;
import com.realestate.plan.enums.FloorType;
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
public class FloorRequestDTO {

    @NotNull(message = "Tower ID is required")
    private Long towerId;

    @NotNull(message = "Floor number is required")
    @Positive(message = "Floor number must be greater than zero")
    private Integer floorNumber;

    @NotNull(message = "Floor type is required")
    private FloorType floorType;

    @NotNull(message = "Floor status is required")
    private FloorStatus floorStatus;

    @NotNull(message = "Total flats is required")
    @PositiveOrZero(message = "Total flats must be zero or positive")
    private Integer totalFlats;

    @DecimalMin(value = "0.0", inclusive = false, message = "Carpet area must be positive")
    private Double carpetArea;

    @DecimalMin(value = "0.0", inclusive = false, message = "Built-up area must be positive")
    private Double builtUpArea;

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
