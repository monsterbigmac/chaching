package com.monsterbigmac.chaching.dto.v1;

import com.monsterbigmac.chaching.entity.FinancialClassification;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialClassificationInfo {

    private Long id;
    private String name;

    public static FinancialClassificationInfo from(FinancialClassification entity) {
        return FinancialClassificationInfo.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
