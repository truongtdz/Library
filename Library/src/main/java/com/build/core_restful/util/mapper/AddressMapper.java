package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Address;
import com.build.core_restful.domain.request.AddressRequest;
import com.build.core_restful.domain.response.AddressResponse;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "user", ignore = true)
    AddressResponse toAddressResponse(Address address);

    @AfterMapping
    default void mapNestedFields(Address address, @MappingTarget AddressResponse.AddressResponseBuilder response) {
        if (address.getUser() != null) {
            response.user(AddressResponse.UserRes.builder()
                    .userId(address.getUser().getId())
                    .userName(address.getUser().getFullName())
                    .build());
        }
    }
    Address toAddress(AddressRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddress(@MappingTarget Address address, AddressRequest request);
}
