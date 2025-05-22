package com.build.core_restful.repository.specification;

import com.build.core_restful.domain.Address;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AddressSpecification {

    public static Specification<Address> filterAddresses(
            String city,
            String district,
            String ward,
            String street,
            String isDefault
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

            if (isDefault != null) {
                predicates.add(cb.equal(root.get("isDefault"), isDefault));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
