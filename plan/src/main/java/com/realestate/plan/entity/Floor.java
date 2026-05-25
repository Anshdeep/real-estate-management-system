package com.realestate.plan.entity;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.FloorStatus;
import com.realestate.plan.enums.FloorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "floors",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"tower_id", "floor_number"},
            name = "UK_floor_tower_number"
        ),
        @UniqueConstraint(
            columnNames = {"floor_code"},
            name = "UK_floor_code"
        )
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String floorCode;

    @Column(nullable = false)
    private Integer floorNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FloorType floorType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FloorStatus floorStatus;

    @Column(nullable = false)
    private Integer totalFlats;

    @Column(nullable = false)
    @Builder.Default
    private Integer occupiedFlats = 0;

    private Double carpetArea;

    @Column
    private Double builtUpArea;

    @Column
    private Double superBuiltUpArea;

    @Enumerated(EnumType.STRING)
    private AreaUnit areaUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tower_id", nullable = false)
    private Tower tower;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate expectedCompletionDate;

    @Column
    private LocalDate actualCompletionDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(length = 100)
    private String createdBy;

    @Column(length = 100)
    private String updatedBy;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
