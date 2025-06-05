package com.build.core_restful.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.build.core_restful.domain.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long>{
    Page<Branch> findAll(Specification<Branch> spec, Pageable pageable);

    Optional<Branch> findByIdAndStatus(Long id, String status);
}
