package com.build.core_restful.repository;

import com.build.core_restful.domain.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<RentalOrder, Long> {
}
