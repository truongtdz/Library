package com.build.core_restful.repository.specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.build.core_restful.domain.Branch;

import jakarta.persistence.criteria.Predicate;

public class BranchSpecification {
     public static Specification<Branch> filterBranches(
            String city,
            String district,
            String ward,
            String street,
            Instant openTime, 
            Instant closeTime
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (city != null && !city.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
            }

            if (district != null && !district.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("district")), "%" + district.toLowerCase() + "%"));
            }

            if (ward != null && !ward.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("ward")), "%" + ward.toLowerCase() + "%"));
            }

            if (street != null && !street.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("street")), "%" + street.toLowerCase() + "%"));
            }

            if (openTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("openTime"), openTime));
            }

            if (closeTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("closeTime"), closeTime));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
