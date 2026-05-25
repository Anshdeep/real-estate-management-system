package com.realestate.plan.entity;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.TowerStatus;
import com.realestate.plan.enums.TowerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "towers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "tower_name"}),
        @UniqueConstraint(columnNames = {"project_id", "tower_code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@lombok.Builder
public class Tower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tower_code", nullable = false, length = 40)
    private String towerCode;

    @Column(name = "tower_name", nullable = false, length = 150)
    private String towerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "tower_type", nullable = false, length = 30)
    private TowerType towerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "tower_status", nullable = false, length = 30)
    private TowerStatus towerStatus;

    @Column(name = "lift_count")
    private Integer liftCount;

    @Column(name = "total_floors")
    private Integer totalFloors;

    @Column(name = "floor_capacity")
    private Integer floorCapacity;

    @Column(name = "total_flats")
    private Integer totalFlats;

    @Column(name = "parking_slots")
    private Integer parkingSlots;

    @Column(name = "built_up_area")
    private Double builtUpArea;

    @Column(name = "carpet_area")
    private Double carpetArea;

    @Column(name = "super_built_up_area")
    private Double superBuiltUpArea;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_unit", length = 20)
    private AreaUnit areaUnit;

    @Column(name = "address_line1", length = 300)
    private String addressLine1;

    @Column(name = "address_line2", length = 300)
    private String addressLine2;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    @lombok.Builder.Default
    private String country = "India";

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "expected_completion_date")
    private LocalDate expectedCompletionDate;

    @Column(name = "actual_completion_date")
    private LocalDate actualCompletionDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "is_deleted")
    @lombok.Builder.Default
    private Boolean isDeleted = false;
}
