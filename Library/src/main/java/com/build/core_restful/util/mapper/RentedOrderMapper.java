package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.RentedOrder;
import com.build.core_restful.domain.request.RentedOrderRequest;
import com.build.core_restful.domain.response.RentedOrderResponse;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RentedOrderMapper {
    RentedOrder toEntity(RentedOrderRequest request);

    RentedOrderResponse toResponse(RentedOrder order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget RentedOrder order, RentedOrderRequest request);
}
