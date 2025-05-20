package com.build.core_restful.repository;

import com.build.core_restful.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorsRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String name);
}
