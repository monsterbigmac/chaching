package com.monsterbigmac.chaching.repository;

import com.monsterbigmac.chaching.entity.FinancialClassification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFinancialClassificationRepository {

    Page<FinancialClassification> search(Long appUserId, String name, Pageable pageable);

}
