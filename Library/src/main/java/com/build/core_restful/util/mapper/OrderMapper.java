package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.RentalOrder;
import com.build.core_restful.domain.request.OrderRequest;
import com.build.core_restful.domain.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    RentalOrder toOrder(OrderRequest orderRequest);
    OrderResponse toOrderResponse(RentalOrder order);
    void updateOrder(@MappingTarget RentalOrder order, OrderRequest orderRequest);
}
