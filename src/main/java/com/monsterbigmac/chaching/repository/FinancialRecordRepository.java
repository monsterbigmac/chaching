package com.monsterbigmac.chaching.repository;

import com.monsterbigmac.chaching.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FinancialRecordRepository
        extends JpaRepository<FinancialRecord, Long>, JpaSpecificationExecutor<FinancialRecord> {

    Optional<FinancialRecord> findByIdAndAppUserId(Long id, Long appUserId);
}
