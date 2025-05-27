package com.build.core_restful.repository.specification;

import com.build.core_restful.domain.RentalOrder;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class RentalOrderSpecification {

    public static Specification<RentalOrder> filterOrders(
            Long fromTotalPrice,
            Long toTotalPrice,
            Long fromDepositPrice,
            Long toDepositPrice,
            Long userId,
            String orderStatus
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }

            if (fromTotalPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalPrice"), fromTotalPrice));
            }

            if (toTotalPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalPrice"), toTotalPrice));
            }

            if (fromDepositPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("depositPrice"), fromDepositPrice));
            }

            if (toDepositPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("depositPrice"), toDepositPrice));
            }

            if (orderStatus != null && !orderStatus.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("orderStatus")), orderStatus.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
