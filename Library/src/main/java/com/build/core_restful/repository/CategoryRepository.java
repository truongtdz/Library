package com.build.core_restful.repository;

import com.build.core_restful.domain.Category;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndStatus(String name, String status);

    Optional<Category> findByIdAndStatus(Long id, String status);

    Page<Category> findByNameAndStatus(String name, String status, Pageable pageable);
}
