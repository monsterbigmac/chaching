package com.monsterbigmac.chaching.service;

import com.monsterbigmac.chaching.entity.AppUser;
import com.monsterbigmac.chaching.entity.FinancialClassification;
import com.monsterbigmac.chaching.exception.ResourceDuplicateException;
import com.monsterbigmac.chaching.exception.ResourceNotFoundException;
import com.monsterbigmac.chaching.repository.FinancialClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FinancialClassificationService {

    @Autowired
    FinancialClassificationRepository financialClassificationRepository;

    public Page<FinancialClassification> search(Long appUserId, String name, Pageable pageable) {
        return financialClassificationRepository.search(appUserId, name, pageable);
    }

    public FinancialClassification createFinancialClassification(Long appUserId, String name) {
        if (financialClassificationRepository.existsByNameAndAppUserId(name, appUserId)) {
            throw new ResourceDuplicateException("Financial Classification already exists");
        }

        FinancialClassification financialClassification = FinancialClassification.builder()
                .appUser(AppUser.builder().id(appUserId).build())
                .name(name)
                .build();
        return financialClassificationRepository.save(financialClassification);
    }

    public FinancialClassification findByIdAndAppUserId(Long id, Long appUserId) {
        return financialClassificationRepository.findByIdAndAppUserId(id, appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Classification not found"));
    }

    public FinancialClassification updateFinancialClassification(Long appUserId, Long id, String name) {
        if (financialClassificationRepository.existsByNameAndAppUserId(name, appUserId)) {
            throw new ResourceDuplicateException("Financial Classification already exists");
        }

        return financialClassificationRepository.findByIdAndAppUserId(id, appUserId)
                .map(financialClassification -> {
                    financialClassification.setName(name);
                    return financialClassificationRepository.save(financialClassification);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Financial Classification not found"));
    }

    public void deleteFinancialClassification(Long id, Long appUserId) {
        financialClassificationRepository.findByIdAndAppUserId(id, appUserId)
                .ifPresentOrElse(financialClassification -> {
                    financialClassificationRepository.delete(financialClassification);
                }, () -> {
                    throw new ResourceNotFoundException("Financial Classification not found");
                });

    }

}
