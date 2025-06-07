package com.build.core_restful.repository.specification;

import com.build.core_restful.domain.Book;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> filterBooks(
            String keyword,
            Long categoryId,
            Long authorId,
            String language,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String status
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (authorId != null) {
                predicates.add(cb.equal(root.get("author").get("id"), authorId));
            }

            if (language != null && !language.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("language")), language.toLowerCase()));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rentalPrice"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("rentalPrice"), maxPrice));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status.toString()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}