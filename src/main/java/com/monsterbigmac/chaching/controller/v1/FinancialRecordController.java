package com.monsterbigmac.chaching.controller.v1;

import com.monsterbigmac.chaching.dto.ApiResponse;
import com.monsterbigmac.chaching.dto.v1.FinancialRecordInfo;
import com.monsterbigmac.chaching.dto.v1.FinancialRecordPayload;
import com.monsterbigmac.chaching.entity.FinancialRecord;
import com.monsterbigmac.chaching.model.SecurityUser;
import com.monsterbigmac.chaching.service.FinancialRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/financial-records")
public class FinancialRecordController {

    @Autowired
    private FinancialRecordService financialRecordService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> search(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String description,
            Pageable pageable,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        Page<FinancialRecord> financialRecords = financialRecordService.search(
                securityUser.getAppUserId(), startDate, endDate, description, pageable);
        List<Map<String, Object>> listByDate = new ArrayList<>();
        financialRecords.stream()
                .map(FinancialRecordInfo::from)
                .collect(Collectors.groupingBy(
                        FinancialRecordInfo::getRecordDate,
                        LinkedHashMap::new,
                        Collectors.toList()
                )).forEach((recordDate, financialRecordInfos) -> {
                    listByDate.add(Map.of(
                            "date", recordDate,
                            "financialRecords", financialRecordInfos
                    ));
                });
        Map<String, Object> data = Map.of(
                "listByDate", listByDate,
                "totalElements", financialRecords.getTotalElements(),
                "totalPages", financialRecords.getTotalPages());
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> createFinancialRecord(
            @Valid @RequestBody FinancialRecordPayload financialRecordPayload,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        FinancialRecord financialRecord = financialRecordService.createFinancialRecord(
                securityUser.getAppUserId(), financialRecordPayload);
        return ResponseEntity.ok(ApiResponse.success(Map.of("financialRecordId", financialRecord.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, FinancialRecordInfo>>> findByIdAndAppUserId(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        FinancialRecordInfo info = FinancialRecordInfo.from(
                financialRecordService.findByIdAndAppUserId(id, securityUser.getAppUserId()));
        return ResponseEntity.ok(ApiResponse.success(Map.of("financialRecord", info)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Long>>> updateFinancialRecord(
            @PathVariable Long id,
            @Valid @RequestBody FinancialRecordPayload financialRecordPayload,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        FinancialRecord financialRecord = financialRecordService.updateFinancialRecord(
                id, securityUser.getAppUserId(), financialRecordPayload);
        return ResponseEntity.ok(ApiResponse.success(Map.of("financialRecordId", financialRecord.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Long>>> deleteFinancialRecord(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        financialRecordService.deleteFinancialRecord(id, securityUser.getAppUserId());
        return ResponseEntity.ok(ApiResponse.success(Map.of("financialRecordId", id)));
    }

}
