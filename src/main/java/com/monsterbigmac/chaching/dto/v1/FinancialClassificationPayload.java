package com.monsterbigmac.chaching.dto.v1;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialClassificationPayload {

    @NotBlank
    private String name;

}
