package com.realestate.plan.dto;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.BHKType;
import com.realestate.plan.enums.FlatStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlatRequestDTO {

    @NotNull(message = "Floor ID is required")
    private Long floorId;

    @NotBlank(message = "Flat number is required")
    private String flatNumber;

    @NotNull(message = "BHK type is required")
    private BHKType bhkType;

    @NotNull(message = "Flat status is required")
    private FlatStatus flatStatus;

    @DecimalMin(value = "0.0", inclusive = true, message = "Built-up area must be non-negative")
    private Double builtUpArea;

    @DecimalMin(value = "0.0", inclusive = true, message = "Super built-up area must be non-negative")
    private Double superBuiltUpArea;

    @DecimalMin(value = "0.0", inclusive = true, message = "Carpet area must be non-negative")
    private Double carpetArea;

    private AreaUnit areaUnit;
}
