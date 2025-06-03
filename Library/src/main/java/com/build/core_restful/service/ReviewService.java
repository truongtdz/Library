package com.build.core_restful.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.request.ReviewRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.ReviewResponse;

@Service
public interface ReviewService {
    PageResponse<Object> getReviewByUserId(Long id, Pageable pageable);

    PageResponse<Object> getReviewByBookId(Long id, Pageable pageable);

    PageResponse<Object> getReviewByParentId(Long id, Pageable pageable);

    ReviewResponse createReview(ReviewRequest request);

    boolean deleteReview(Long id);
}
