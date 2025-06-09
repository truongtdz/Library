package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.UserResponse;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "role", ignore = true)
    UserResponse toUserResponse(User user);

    @AfterMapping
    default void mapNestedFields(User user, @MappingTarget UserResponse.UserResponseBuilder response) {
        if (user.getRole() != null) {
            response.role(UserResponse.RoleRes.builder()
                    .roleId(user.getRole().getId())
                    .roleName(user.getRole().getName())
                    .build());
        }
        if (user.getRentalOrders() != null) {
            response.totalRental(Long.valueOf(user.getRentalOrders().size()));
        }
        if (user.getRentedOrders() != null) {
            response.totalRented(Long.valueOf(user.getRentedOrders().size()));
        }
    }

    User toUser(UserRequest userRequest);

    void updateUser(@MappingTarget User user, UserRequest userRequest);
}