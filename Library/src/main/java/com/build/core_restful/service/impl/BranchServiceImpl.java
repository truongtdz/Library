package com.build.core_restful.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Branch;
import com.build.core_restful.domain.RentalItem;
import com.build.core_restful.domain.RentalOrder;
import com.build.core_restful.domain.RentedOrder;
import com.build.core_restful.domain.request.BranchRequest;
import com.build.core_restful.domain.response.BranchResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.BranchRepository;
import com.build.core_restful.repository.RentalOrderRepository;
import com.build.core_restful.repository.RentedOrderRepository;
import com.build.core_restful.repository.specification.BranchSpecification;
import com.build.core_restful.service.BranchService;
import com.build.core_restful.util.enums.EntityStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.BranchMapper;

@Service
public class BranchServiceImpl implements BranchService{
    private final BranchRepository branchRepository;
    private final RentalOrderRepository rentalOrderRepository;
    private final RentedOrderRepository rentedOrderRepository;
    private final BranchMapper branchMapper;

    public BranchServiceImpl(
        BranchRepository branchRepository,
        RentalOrderRepository rentalOrderRepository,
        RentedOrderRepository rentedOrderRepository,
        BranchMapper branchMapper
    ){
        this.branchRepository = branchRepository;
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentedOrderRepository = rentedOrderRepository;
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
            String status,
            Pageable pageable
    ) {
        Specification<Branch> spec = BranchSpecification.filterBranches(
            city, district, ward, street, openTime, closeTime, status
        );

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
    public boolean softDeleteBranch(List<Long> idList){
        try {
            for(Long id : idList){
                Branch branch = branchRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                    .orElseThrow(() -> new NewException("Branch with id: " + id + " not found"));
                    
                branch.setStatus(EntityStatusEnum.Delete.toString());

                branchRepository.save(branch);
            }
            return true;
        } catch (Exception e) {
            throw new NewException("Error soft deleting books: " + e.getMessage());
        }
    }
    
    @Override
    public boolean restoreBranch(List<Long> idList){
        try {
            for(Long id : idList){
                Branch branch = branchRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                    .orElseThrow(() -> new NewException("Branch with id: " + id + " not found"));
                    
                branch.setStatus(EntityStatusEnum.Active.toString());

                branchRepository.save(branch);
            }
            return true;
        } catch (Exception e) {
            throw new NewException("Error soft deleting books: " + e.getMessage());
        }
    }

    @Override
    public void deleteBranch(Long id) {
        List<RentalOrder> rentalOrder = rentalOrderRepository.findAllByUserId(id);
        rentalOrder.forEach(item -> item.setBranch(null));
        rentalOrderRepository.saveAll(rentalOrder);

        List<RentedOrder> rentedOrder = rentedOrderRepository.findAllByUserId(id);
        rentedOrder.forEach(item -> item.setBranch(null));
        rentedOrderRepository.saveAll(rentedOrder);

        branchRepository.deleteById(id);
    }
    
}
