package com.build.core_restful.repository;

import com.build.core_restful.domain.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findByUserId(Long id, Pageable pageable);

    Address findByIdAndUserId(Long id, Long userId);

    List<Address> findByUserId(Long userId);
}
