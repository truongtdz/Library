package com.build.core_restful.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.build.core_restful.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    Page<Review> findByUserId(Long id, Pageable pageable);

    Page<Review> findByBookId(Long id, Pageable pageable);
    
    Page<Review> findByParentId(Long id, Pageable pageable);

    List<Review> findByParentId(Long id);
}
