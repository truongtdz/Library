package com.build.core_restful.repository;

import com.build.core_restful.domain.Author;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByNameAndStatus(String name, String status);

    Optional<Author> findByIdAndStatus(Long id, String status);

    Page<Author> findByNameAndStatus(String name, String status, Pageable pageable);
}
