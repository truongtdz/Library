package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.RentedOrder;
import com.build.core_restful.domain.request.RentedOrderRequest;
import com.build.core_restful.domain.response.RentedOrderResponse;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RentalItemMapper.class})
public interface RentedOrderMapper {
    RentedOrder toEntity(RentedOrderRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "items", target = "items") 
    RentedOrderResponse toResponse(RentedOrder order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget RentedOrder order, RentedOrderRequest request);
}
