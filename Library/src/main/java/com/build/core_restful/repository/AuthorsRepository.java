package com.build.core_restful.repository;

import com.build.core_restful.domain.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorsRepository extends JpaRepository<Authors, Long> {
    boolean existsByName(String name);
}
