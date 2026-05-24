package com.realestate.plan.repository;

import com.realestate.plan.entity.Builder;
import com.realestate.plan.enums.BuilderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuilderRepository extends JpaRepository<Builder, Long> {

    Optional<Builder> findByBuilderCodeAndIsDeletedFalse(String builderCode);

    Optional<Builder> findByEmailIdAndIsDeletedFalse(String emailId);

    Optional<Builder> findByGstNumberAndIsDeletedFalse(String gstNumber);

    Optional<Builder> findByPanNumberAndIsDeletedFalse(String panNumber);

    Optional<Builder> findByReraNumberAndIsDeletedFalse(String reraNumber);

    List<Builder> findByStatusAndIsDeletedFalse(BuilderStatus status);

    List<Builder> findByIsDeletedFalse();

    List<Builder> findByCityAndIsDeletedFalse(String city);

    List<Builder> findByStateAndIsDeletedFalse(String state);

    boolean existsByBuilderCode(String builderCode);

    boolean existsByEmailId(String emailId);

    boolean existsByGstNumber(String gstNumber);

    boolean existsByPanNumber(String panNumber);

    boolean existsByReraNumber(String reraNumber);

    @Query("SELECT COUNT(b) FROM Builder b WHERE b.isDeleted = false AND b.status = 'ACTIVE'")
    long countActiveBuilders();

    @Query("SELECT b FROM Builder b WHERE b.isDeleted = false AND " +
           "(LOWER(b.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.brandName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.city) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Builder> searchBuilders(String keyword);
}
