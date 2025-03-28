package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UserCreateRequest;
import com.build.core_restful.domain.request.UserUpdateRequest;
import com.build.core_restful.domain.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest userRequest);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest userRequest);
}
