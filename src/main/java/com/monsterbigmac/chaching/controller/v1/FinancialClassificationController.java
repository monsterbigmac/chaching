package com.monsterbigmac.chaching.controller.v1;

import com.monsterbigmac.chaching.dto.ApiResponse;
import com.monsterbigmac.chaching.dto.v1.FinancialClassificationInfo;
import com.monsterbigmac.chaching.dto.v1.FinancialClassificationPayload;
import com.monsterbigmac.chaching.entity.FinancialClassification;
import com.monsterbigmac.chaching.model.SecurityUser;
import com.monsterbigmac.chaching.service.FinancialClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/financial-classifications")
public class FinancialClassificationController {

    @Autowired
    private FinancialClassificationService financialClassificationService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> search(
            @RequestParam(required = false) String name,
            Pageable pageable,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        Page<FinancialClassification> financialClassifications = financialClassificationService
                .search(securityUser.getAppUserId(), name, pageable);
        Map<String, Object> data = Map.of(
                "financialClassifications", financialClassifications.stream()
                        .map(FinancialClassificationInfo::from).toList(),
                "totalElements", financialClassifications.getTotalElements(),
                "totalPages", financialClassifications.getTotalPages());
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> createFinancialClassification(
            @RequestBody FinancialClassificationPayload payload,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        FinancialClassification financialClassification = financialClassificationService
                .createFinancialClassification(securityUser.getAppUserId(), payload.getName());
        return ResponseEntity.ok(
                ApiResponse.success(Map.of("financialClassificationId", financialClassification.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, FinancialClassificationInfo>>> findByIdAndAppUserId(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        FinancialClassificationInfo info = FinancialClassificationInfo.from(
                financialClassificationService.findByIdAndAppUserId(id, securityUser.getAppUserId()));
        return ResponseEntity.ok(ApiResponse.success(Map.of("financialClassification", info)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Long>>> updateFinancialClassification(
            @PathVariable Long id,
            @RequestBody FinancialClassificationPayload payload,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        FinancialClassification financialClassification = financialClassificationService
                .updateFinancialClassification(securityUser.getAppUserId(), id, payload.getName());
        return ResponseEntity.ok(
                ApiResponse.success(Map.of("financialClassificationId", financialClassification.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Long>>> deleteFinancialClassification(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        financialClassificationService.deleteFinancialClassification(id, securityUser.getAppUserId());
        return ResponseEntity.ok(ApiResponse.success(Map.of("financialClassificationId", id)));
    }

}
