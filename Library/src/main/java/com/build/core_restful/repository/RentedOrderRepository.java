package com.build.core_restful.repository;

import com.build.core_restful.domain.RentedOrder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentedOrderRepository extends JpaRepository<RentedOrder, Long> {
    Page<RentedOrder> findAll(Specification<RentedOrder> spec, Pageable pageable);

    List<RentedOrder> findAllByUserId(Long id);

    @EntityGraph(attributePaths = {"items"})
    @Query("SELECT r FROM RentedOrder r WHERE r.id = :id")
    Optional<RentedOrder> findWithItems(@Param("id") Long id);

}
