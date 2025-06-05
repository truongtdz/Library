package com.build.core_restful.service;

import java.time.Instant;
import java.util.List;

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
        String status,
        Pageable pageable
    );

    BranchResponse getBranchById(Long id);

    BranchResponse createBranch(BranchRequest request);

    BranchResponse updateBranch(Long id, BranchRequest request);

    boolean softDeleteBranch(List<Long> idList);
    
    boolean restoreBranch(List<Long> idList);

    void deleteBranch(Long id);
} 
