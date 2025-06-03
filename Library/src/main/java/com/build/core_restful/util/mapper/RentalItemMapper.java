package com.build.core_restful.util.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.build.core_restful.domain.RentalItem;
import com.build.core_restful.domain.request.RentalItemRequest;
import com.build.core_restful.domain.response.RentalItemResponse;

@Mapper(componentModel = "spring")
public interface RentalItemMapper {
    RentalItem toEntity(RentalItemRequest request);

    RentalItemResponse toResponse(RentalItem rentalItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget RentalItem rentalItem, RentalItemRequest request);
}
