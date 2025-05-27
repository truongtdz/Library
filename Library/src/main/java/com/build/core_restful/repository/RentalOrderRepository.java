package com.build.core_restful.repository;

import com.build.core_restful.domain.RentalOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {
    Page<RentalOrder> findAll(Specification<RentalOrder> spec, Pageable pageable);
}
