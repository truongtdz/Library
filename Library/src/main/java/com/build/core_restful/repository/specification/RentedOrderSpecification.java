package com.build.core_restful.repository.specification;

import com.build.core_restful.domain.RentedOrder;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class RentedOrderSpecification {

    public static Specification<RentedOrder> filterOrders(
            Long fromLateFee,
            Long toLateFee,
            Long userId,
            String orderStatus
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }

            if (fromLateFee != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("LateFee"), fromLateFee));
            }

            if (toLateFee != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("LateFee"), toLateFee));
            }

            if (orderStatus != null && !orderStatus.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("orderStatus")), orderStatus.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
