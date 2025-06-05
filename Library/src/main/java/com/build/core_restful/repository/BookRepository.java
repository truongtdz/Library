package com.build.core_restful.repository;

import com.build.core_restful.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    Optional<Book> findByIdAndStatus(Long id, String status);

    Page<Book> findByStatus(String status, Pageable pageable);

    List<Book> findTop10ByOrderByQuantityRentedDesc();

    List<Book> findTop10ByOrderByQuantityViewedDesc();

    List<Book> findTop10ByOrderByQuantityLikedDesc();

    List<Book> findByCategoryId(Long id);
}
