package com.monsterbigmac.chaching.dto.v1;

import com.monsterbigmac.chaching.entity.FinancialRecord;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Validated
public class FinancialRecordPayload {

    private Long Id;

    @NotNull(message = "{validation.amount.notNull}")
    private BigDecimal amount;

    @NotNull(message = "{validation.cashFlowType.notNull}")
    private FinancialRecord.CashFlowType cashFlowType;

    @NotBlank(message = "{validation.description.notBlank}")
    private String description;

    @NotNull(message = "{validation.recordDate.notNull}")
    private LocalDate recordDate;

    @NotNull(message = "{validation.classificationId.notNull}")
    private Long classificationId;

}
