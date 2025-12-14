package com.monsterbigmac.chaching.service;

import com.monsterbigmac.chaching.dto.v1.FinancialRecordPayload;
import com.monsterbigmac.chaching.entity.AppUser;
import com.monsterbigmac.chaching.entity.FinancialClassification;
import com.monsterbigmac.chaching.entity.FinancialRecord;
import com.monsterbigmac.chaching.exception.ResourceNotFoundException;
import com.monsterbigmac.chaching.repository.FinancialRecordRepository;
import com.monsterbigmac.chaching.specification.FinancialRecordSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FinancialRecordService {

    @Autowired
    private FinancialRecordRepository financialRecordRepository;

    public Page<FinancialRecord> search(
            Long appUserId,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            Pageable pageable
    ) {
        List<Sort.Order> orders = pageable.getSort().stream().map(o -> new Sort.Order(
                        o.getDirection(),
                        SortFields.contains(o.getProperty()) ? o.getProperty() : SortFields.ID.fieldName)).toList();
        Specification<FinancialRecord> spec = Specification
                .where(FinancialRecordSpecification.hasAppUserId(appUserId))
                .and(FinancialRecordSpecification.recordDateRangeBetween(startDate, endDate))
                .and(FinancialRecordSpecification.hasDescriptionLike(description))
                .and(FinancialRecordSpecification.leftJoinClassification());

        return financialRecordRepository
                .findAll(spec, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders)));
    }

    private enum SortFields {
        ID("id"),
        RECORD_DATE("recordDate");

        private final String fieldName;

        SortFields(String fieldName) {
            this.fieldName = fieldName;
        }

        private static boolean contains(String field) {
            for (SortFields value : values()) {
                if (value.fieldName.equals(field)) {
                    return true;
                }
            }
            return false;
        }
    }

    public FinancialRecord findByIdAndAppUserId(Long id, Long appUserId) {
        return financialRecordRepository.findByIdAndAppUserId(id, appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Record not found"));
    }

    public FinancialRecord createFinancialRecord(Long appUserId, FinancialRecordPayload financialRecordPayload) {
        FinancialClassification financialClassification = FinancialClassification.builder()
                .id(financialRecordPayload.getClassificationId())
                .build();
        FinancialRecord financialRecord = FinancialRecord.builder()
                .appUser(AppUser.builder().id(appUserId).build())
                .amount(financialRecordPayload.getAmount())
                .cashFlowType(financialRecordPayload.getCashFlowType())
                .description(financialRecordPayload.getDescription())
                .recordDate(financialRecordPayload.getRecordDate())
                .classification(financialClassification)
                .build();
        return financialRecordRepository.save(financialRecord);
    }

    public FinancialRecord updateFinancialRecord(
            Long id, Long appUserId, FinancialRecordPayload financialRecordPayload
    ) {
        return financialRecordRepository.findByIdAndAppUserId(id, appUserId)
                .map(financialRecord -> {
                    FinancialClassification financialClassification = FinancialClassification.builder()
                            .id(financialRecordPayload.getClassificationId())
                            .build();
                    financialRecord.setAmount(financialRecordPayload.getAmount());
                    financialRecord.setCashFlowType(financialRecordPayload.getCashFlowType());
                    financialRecord.setDescription(financialRecordPayload.getDescription());
                    financialRecord.setRecordDate(financialRecordPayload.getRecordDate());
                    financialRecord.setClassification(financialClassification);

                    return financialRecordRepository.save(financialRecord);
                }).orElseThrow(() -> new ResourceNotFoundException("Financial Record not found"));
    }

    public void deleteFinancialRecord(Long id, Long appUserId) {
        financialRecordRepository.findByIdAndAppUserId(id, appUserId)
                .ifPresent(financialRecord -> {
                    financialRecordRepository.delete(financialRecord);
                });
    }

}
