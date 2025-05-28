package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.RentedOrder;
import com.build.core_restful.domain.request.RentedOrderRequest;
import com.build.core_restful.domain.response.RentedOrderResponse;

import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RentedOrderMapper {
    RentedOrder toEntity(RentedOrderRequest request);

    RentedOrderResponse toResponse(RentedOrder order);

    @AfterMapping
    default void mapNestedFields(RentedOrder rentedOrder, @MappingTarget RentedOrderResponse.RentedOrderResponseBuilder response) {
        if(rentedOrder.getUser() != null){
            response.userId(rentedOrder.getUser().getId());
        }
        
        if (rentedOrder.getItems() != null) {
        List<RentedOrderResponse.RentalItemRes> rentalItemRes = rentedOrder.getItems().stream()
            .map(item -> RentedOrderResponse.RentalItemRes.builder()
                .itemId(item.getId())
                .orderId(item.getRentalOrder().getId())
                .build())
            .toList();
        response.items(rentalItemRes);
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget RentedOrder order, RentedOrderRequest request);
}
