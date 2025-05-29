package com.build.core_restful.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.build.core_restful.domain.Branch;


public interface BranchRepository extends JpaRepository<Branch, Long>{
    Page<Branch> findAll(Specification<Branch> spec, Pageable pageable);
}
