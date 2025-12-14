package com.monsterbigmac.chaching.dto.v1;

import com.monsterbigmac.chaching.entity.FinancialRecord;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class FinancialRecordInfo {

    private Long id;
    private BigDecimal amount;
    private FinancialRecord.CashFlowType cashFlowType;
    private String description;
    private LocalDate recordDate;
    private Map<String, String> classification;

    public static FinancialRecordInfo from(FinancialRecord entity) {
        return FinancialRecordInfo.builder()
                .id(entity.getId())
                .amount((entity.getAmount()))
                .cashFlowType(entity.getCashFlowType())
                .description(entity.getDescription())
                .recordDate(entity.getRecordDate())
                .classification(Map.of("name", entity.getClassification().getName()))
                .build();
    }

}