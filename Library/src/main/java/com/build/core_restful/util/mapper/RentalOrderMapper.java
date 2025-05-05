package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.RentalOrder;
import com.build.core_restful.domain.request.RentalOrderRequest;
import com.build.core_restful.domain.response.RentalOrderResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AddressMapper.class})
public interface RentalOrderMapper {
    RentalOrder toEntity(RentalOrderRequest request);

    @Mapping(source = "user.id", target = "userId")
    RentalOrderResponse toResponse(RentalOrder order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget RentalOrder order, RentalOrderRequest request);
}
