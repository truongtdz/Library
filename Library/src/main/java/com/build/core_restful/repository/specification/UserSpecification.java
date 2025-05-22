package com.build.core_restful.repository.specification;

import com.build.core_restful.domain.User;
import com.build.core_restful.util.enums.GenderEnum;
import com.build.core_restful.util.enums.UserStatusEnum;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filterUsers(
            String keyword,
            GenderEnum gender,
            Long roleId,
            UserStatusEnum userStatus
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                Predicate emailPredicate = cb.like(cb.lower(root.get("email")), likeKeyword);
                Predicate namePredicate = cb.like(cb.lower(root.get("fullName")), likeKeyword);
                predicates.add(cb.or(emailPredicate, namePredicate));
            }

            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            if (userStatus != null) {
                predicates.add(cb.equal(root.get("status"), userStatus));
            }

            if (roleId != null) {
                predicates.add(cb.equal(root.get("role").get("id"), roleId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
