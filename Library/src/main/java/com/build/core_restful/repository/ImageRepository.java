package com.build.core_restful.repository;

import com.build.core_restful.domain.Image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByIdAndBookId(Long imageId, Long bookId);
    List<Image> findByBookId(Long bookId);
}
