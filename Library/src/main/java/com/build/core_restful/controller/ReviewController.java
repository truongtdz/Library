package com.build.core_restful.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.domain.request.ReviewRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.ReviewService;
import com.build.core_restful.util.annotation.AddMessage;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(
        ReviewService reviewService
    ){
        this.reviewService = reviewService;
    }

    @GetMapping("/by/user/{id}")
    @AddMessage("Get reviews by user")
    public ResponseEntity<PageResponse<Object>> getReviewByUser(
        @PathVariable Long id, 
        @RequestParam int page, 
        @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reviewService.getReviewByUserId(id, pageable));
    }

    @GetMapping("/by/book/{id}")
    @AddMessage("Get reviews by id")
    public ResponseEntity<Object> getReviewByBook(
        @PathVariable Long id, 
        @RequestParam int page, 
        @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reviewService.getReviewByBookId(id, pageable));
    }

    @GetMapping("/by/parent/{id}")
    @AddMessage("Get reviews by id")
    public ResponseEntity<Object> getReviewByParent(
        @PathVariable Long id, 
        @RequestParam int page, 
        @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reviewService.getReviewByParentId(id, pageable));
    }

    @PostMapping("/create")
    @AddMessage("Create review")
    public ResponseEntity<Object> createAddress(@RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @DeleteMapping("/delete/{id}")
    @AddMessage("Delete review")
    public ResponseEntity<Object> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}
