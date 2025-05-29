package com.build.core_restful.service;

import java.time.Instant;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.request.BranchRequest;
import com.build.core_restful.domain.response.BranchResponse;
import com.build.core_restful.domain.response.PageResponse;

@Service
public interface BranchService {
    PageResponse<Object> getAllBranch(
        String city, 
        String district, 
        String ward, 
        String street, 
        Instant openTime, 
        Instant closeTime,
        Pageable pageable
    );

    BranchResponse getBranchById(Long id);

    BranchResponse createBranch(BranchRequest request);

    BranchResponse updateBranch(Long id, BranchRequest request);

    boolean deleteBranch(Long id);
} 
