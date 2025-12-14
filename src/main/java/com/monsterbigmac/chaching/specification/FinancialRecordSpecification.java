package com.monsterbigmac.chaching.specification;

import com.monsterbigmac.chaching.entity.FinancialRecord;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class FinancialRecordSpecification {

    public static Specification<FinancialRecord> hasAppUserId(Long appUserId) {
        return (root, query, builder) -> {
            if (appUserId == null) {
                return builder.conjunction();
            }

            return builder.equal(root.get("appUser").get("id"), appUserId);
        };
    }

    public static Specification<FinancialRecord> recordDateRangeBetween(
            LocalDate startDate, LocalDate endDate
    ) {
        return (root, query, builder) -> {
            if (startDate == null && endDate == null) {
                return builder.conjunction();
            }

            if (startDate == null) {
                return builder.lessThanOrEqualTo(root.get("recordDate"), endDate);
            }

            if (endDate == null) {
                return builder.greaterThanOrEqualTo(root.get("recordDate"), startDate);
            }

            return builder.between(root.get("recordDate"), startDate, endDate);
        };
    }

    public static Specification<FinancialRecord> hasDescriptionLike(String description) {
        return (root, query, builder) -> {
            if (description == null || description.isEmpty()) {
                return builder.conjunction();
            }

            return builder.like(root.get("description"), "%" + description + "%");
        };
    }

    public static Specification<FinancialRecord> leftJoinClassification() {
        return (root, query, builder) -> {
            // Only apply fetch for non-count queries
            if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("classification", JoinType.LEFT);
            } else {
                // For count queries, use regular join instead of fetch
                root.join("classification", JoinType.LEFT);
            }

            return builder.conjunction();
        };
    }

}