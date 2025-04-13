package com.build.core_restful.repository;

import com.build.core_restful.domain.RentalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<RentalItem, Long> {
}
