package com.build.core_restful.repository;

import com.build.core_restful.domain.Author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String name);

    Page<Author> findByName(String name, Pageable pageable);
}
