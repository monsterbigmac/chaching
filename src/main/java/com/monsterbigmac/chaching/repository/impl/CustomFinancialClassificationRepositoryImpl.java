package com.monsterbigmac.chaching.repository.impl;

import com.monsterbigmac.chaching.entity.FinancialClassification;
import com.monsterbigmac.chaching.repository.CustomFinancialClassificationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class CustomFinancialClassificationRepositoryImpl implements CustomFinancialClassificationRepository {

    @Autowired
    EntityManager entityManager;

    public Page<FinancialClassification> search(Long appUserId, String name, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FinancialClassification> entityQuery = criteriaBuilder
                .createQuery(FinancialClassification.class);
        Root<FinancialClassification> root = entityQuery.from(FinancialClassification.class);
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);
        Root<FinancialClassification> countRoot = countQuery.from(FinancialClassification.class);
        List<Predicate> countPredicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("appUser").get("id"), appUserId));
        countPredicates.add(criteriaBuilder.equal(countRoot.get("appUser").get("id"), appUserId));
        if (name != null && !name.isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name +"%"));
            countPredicates.add(criteriaBuilder.like(countRoot.get("name"), "%" + name +"%"));
        }

        entityQuery
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
        TypedQuery<FinancialClassification> typedQuery = entityManager.createQuery(entityQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<FinancialClassification> financialClassifications = typedQuery.getResultList();

        countQuery
                .select(criteriaBuilder.count(countRoot))
                .where(criteriaBuilder.and(countPredicates.toArray(new Predicate[] {})));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(financialClassifications, pageable, totalCount);
    }

}
