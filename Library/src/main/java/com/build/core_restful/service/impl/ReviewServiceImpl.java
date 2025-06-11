package com.build.core_restful.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.Review;
import com.build.core_restful.domain.request.ReviewRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.ReviewResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.ReviewRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.service.ReviewService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.ReviewMapper;

@Service
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl (
        ReviewRepository reviewRepository,
        ReviewMapper reviewMapper,
        BookRepository bookRepository,
        UserRepository userRepository
    ){
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    };

    @Override
    public PageResponse<Object> getReviewByUserId(Long id, Pageable pageable) {
        Page<Review> page = reviewRepository.findByUserId(id, pageable);

        Page<ReviewResponse> pageResponse = page.map(reviewMapper::toReviewResponse);

        return PageResponse.builder()
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public PageResponse<Object> getReviewByBookId(Long id, Pageable pageable) {
        Page<Review> page = reviewRepository.findByBookId(id, pageable);

        Page<ReviewResponse> pageResponse = page.map(reviewMapper::toReviewResponse);

        return PageResponse.builder()
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public PageResponse<Object> getReviewByParentId(Long id, Pageable pageable) {
        Page<Review> page = reviewRepository.findByParentId(id, pageable);

        Page<ReviewResponse> pageResponse = page.map(reviewMapper::toReviewResponse);

        return PageResponse.builder()
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public ReviewResponse createReview(ReviewRequest request) {
        Review newReview = Review.builder()
            .image(request.getImage())
            .rate(request.getRate())
            .comment(request.getComment())
            .parentId(request.getParentId())
            .book(bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NewException("Book with id = " + request.getBookId() + " not found"))
            )
            .user(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NewException("User with id = " + request.getUserId() + " no found"))
            )
            .build();

        return reviewMapper.toReviewResponse(reviewRepository.save(newReview));
    }

    @Override
    public boolean deleteReview(Long id) {
        try {
            List<Review> reviews = reviewRepository.findByParentId(id);
            reviewRepository.deleteAll(reviews);
            reviewRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new NewException("Delete review fail");
        }
    }
    
}
