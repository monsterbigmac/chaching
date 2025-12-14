package com.monsterbigmac.chaching.repository;

import com.monsterbigmac.chaching.entity.FinancialClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FinancialClassificationRepository extends JpaRepository<FinancialClassification, Long>, CustomFinancialClassificationRepository {

    Optional<FinancialClassification> findByIdAndAppUserId(Long id, Long appUserId);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                FROM FinancialClassification fc
                WHERE fc.name = :name AND fc.appUser.id = :appUserId
            )
            """)
    Boolean existsByNameAndAppUserId(@Param("name") String name, @Param("appUserId") Long appUserId);

    @Modifying
    @Transactional
    @Query("DELETE FROM FinancialClassification fc WHERE fc.id IN :ids AND fc.appUser.id = :appUserId")
    int deleteByAppUserIdAndIdIn(@Param("appUserId") Long appUserId, @Param("ids") List<Long> ids);

}
