package com.build.core_restful.util.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.build.core_restful.domain.Branch;
import com.build.core_restful.domain.request.BranchRequest;
import com.build.core_restful.domain.response.BranchResponse;


@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchResponse toBranchResponse(Branch branch);

    Branch toBranch(BranchRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBranch(@MappingTarget Branch Branch, BranchRequest request);
}
