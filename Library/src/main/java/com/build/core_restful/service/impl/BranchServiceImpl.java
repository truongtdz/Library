package com.build.core_restful.service.impl;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.Branch;
import com.build.core_restful.domain.request.BranchRequest;
import com.build.core_restful.domain.response.BranchResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.BranchRepository;
import com.build.core_restful.repository.specification.BranchSpecification;
import com.build.core_restful.service.BranchService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.BranchMapper;

@Service
public class BranchServiceImpl implements BranchService{
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public BranchServiceImpl(
        BranchRepository branchRepository,
        BranchMapper branchMapper
    ){
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    };

    @Override
    public PageResponse<Object> getAllBranch(
            String city, 
            String district, 
            String ward, 
            String street,
            Instant openTime, 
            Instant closeTime,
            Pageable pageable
    ) {
        Specification<Branch> spec = BranchSpecification.filterBranches(city, district, ward, street, openTime, closeTime);

        Page<Branch> page = branchRepository.findAll(spec, pageable);

        Page<BranchResponse> pageResponse = page.map(branchMapper::toBranchResponse);

        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public BranchResponse getBranchById(Long id) {
        return branchMapper.toBranchResponse(
            branchRepository.findById(id)
            .orElseThrow(() -> new NewException("Branch with id = " + id + " not found"))
        );
    }

    @Override
    public BranchResponse createBranch(BranchRequest request) {
        Branch branch = branchRepository.save(branchMapper.toBranch(request));
        return branchMapper.toBranchResponse(branch);
    }

    @Override
    public BranchResponse updateBranch(Long id, BranchRequest request) {
        Branch currentBranch = branchRepository.findById(id)
            .orElseThrow(() -> new NewException("Branch with id = " + id + " not found"));

        branchMapper.updateBranch(currentBranch, request);

        return branchMapper.toBranchResponse(branchRepository.save(currentBranch));
    }

    @Override
    public boolean deleteBranch(Long id) {
        try {
            branchRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new NewException("Delete branch fail");
        }
    }
    
}
