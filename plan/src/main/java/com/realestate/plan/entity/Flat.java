package com.realestate.plan.entity;

import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.BHKType;
import com.realestate.plan.enums.FlatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "flats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"floor_id", "flat_number"}, name = "UK_flat_floor_number"),
        @UniqueConstraint(columnNames = {"flat_code"}, name = "UK_flat_code")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flat_code", nullable = false, length = 40, unique = true)
    private String flatCode;

    @Column(name = "flat_number", nullable = false, length = 50)
    private String flatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bhk_type", length = 30)
    private BHKType bhkType;

    @Enumerated(EnumType.STRING)
    @Column(name = "flat_status", length = 30)
    private FlatStatus flatStatus;

    @Column(name = "built_up_area")
    private Double builtUpArea;

    @Column(name = "super_built_up_area")
    private Double superBuiltUpArea;

    @Column(name = "carpet_area")
    private Double carpetArea;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_unit", length = 20)
    private AreaUnit areaUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
