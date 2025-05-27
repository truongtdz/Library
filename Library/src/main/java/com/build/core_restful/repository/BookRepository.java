package com.build.core_restful.repository;

import com.build.core_restful.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    List<Book> findTop10ByOrderByQuantitySellDesc();

    List<Book> findTop10ByOrderByQuantityViewDesc();

    List<Book> findTop10ByOrderByQuantityLikeDesc();

    @Query("SELECT SUM(b.totalQuantity) FROM Book b")
    Integer getTotalBooks();

    @Query("SELECT SUM(b.totalQuantity - b.stock) FROM Book b")
    Integer getQuantityBookRetailing();

    @Query("SELECT SUM(b.totalQuantity - b.stock) FROM Book b")
    Integer getQuantityBookLated();

}
