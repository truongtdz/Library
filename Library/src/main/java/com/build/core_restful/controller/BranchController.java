package com.build.core_restful.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.domain.request.BranchRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.BranchService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.EntityStatusEnum;

@RestController
@RequestMapping("/api/v1/branch")
public class BranchController {
    private final BranchService branchService;

    public BranchController(
        BranchService branchService
    ){
        this.branchService = branchService;
    }

    @GetMapping("/all")
    @AddMessage("Get all branches")
    public ResponseEntity<PageResponse<Object>> getAllAddress(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String district,
        @RequestParam(required = false) String ward,
        @RequestParam(required = false) String street,
        @RequestParam(required = false) Instant openTime,
        @RequestParam(required = false) Instant closeTime,
        @RequestParam(defaultValue = "Active") EntityStatusEnum status
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(branchService.getAllBranch(
            city, district, ward, street, openTime, closeTime, status.toString(), pageable
        ));
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get branch by id")
    public ResponseEntity<Object> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @PostMapping("/create")
    @AddMessage("Create branch")
    public ResponseEntity<Object> createAddress(@RequestBody BranchRequest request) {
        return ResponseEntity.ok(branchService.createBranch(request));
    }

    @PutMapping("/update/{id}")
    @AddMessage("Update branch")
    public ResponseEntity<Object> updateAddress(@PathVariable Long id, @RequestBody BranchRequest request) {
        return ResponseEntity.ok(branchService.updateBranch(id, request));
    }

    @PutMapping("/delete")
    @AddMessage("Soft delete branch")
    public ResponseEntity<Object> SoftDeleteBranch(@RequestBody List<Long> idList) {
        return ResponseEntity.ok(branchService.softDeleteBranch(idList));
    }

    @PutMapping("/restore")
    @AddMessage("Restore branch")
    public ResponseEntity<Object> RestoreBranch(@RequestBody List<Long> idList) {
        return ResponseEntity.ok(branchService.restoreBranch(idList));
    }
}
