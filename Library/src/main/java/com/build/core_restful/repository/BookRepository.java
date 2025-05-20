package com.build.core_restful.repository;

import com.build.core_restful.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByName(String name);

    Page<Book> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Book> findByCategoryId(Long id, Pageable pageable);

    Page<Book> findByAuthorsId(Long id, Pageable pageable);
}
