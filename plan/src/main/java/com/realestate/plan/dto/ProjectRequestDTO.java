package com.realestate.plan.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.ProjectPhase;
import com.realestate.plan.enums.ProjectStatus;
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
public class ProjectRequestDTO {

    @NotBlank(message = "Project name is required")
    @Size(max = 200, message = "Project name must not exceed 200 characters")
    private String projectName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    private ProjectPhase phase;

    @NotNull(message = "Builder ID is required")
    private Long builderId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Total area must be positive")
    private Double totalArea;

    @DecimalMin(value = "0.0", inclusive = false, message = "Built-up area must be positive")
    private Double builtUpArea;

    private AreaUnit areaUnit;

    @DecimalMin(value = "0.0", inclusive = false, message = "Plot area must be positive")
    private Double plotArea;

    @JsonAlias({"address", "addressLine1"})
    @Size(max = 300)
    private String addressLine1;

    @JsonAlias({"addressLine2", "address_2"})
    @Size(max = 300)
    private String addressLine2;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String country;

    @JsonAlias({"zipcode", "postalCode", "pin"})
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid pincode")
    private String pincode;

    private Double latitude;
    private Double longitude;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @JsonAlias({"endDate", "completionDate", "expectedEndDate"})
    private LocalDate expectedCompletionDate;

    private LocalDate actualCompletionDate;

    @AssertTrue(message = "Expected completion date must be on or after start date")
    public boolean isExpectedCompletionDateValid() {
        if (startDate == null || expectedCompletionDate == null) {
            return true;
        }
        return !expectedCompletionDate.isBefore(startDate);
    }

    @AssertTrue(message = "Actual completion date must not be before start date")
    public boolean isActualCompletionDateValid() {
        if (startDate == null || actualCompletionDate == null) {
            return true;
        }
        return !actualCompletionDate.isBefore(startDate);
    }

    @AssertTrue(message = "Area unit is required when any area value is provided")
    public boolean isAreaUnitRequiredWhenAreaPresent() {
        if (totalArea != null || builtUpArea != null || plotArea != null) {
            return areaUnit != null;
        }
        return true;
    }
}
