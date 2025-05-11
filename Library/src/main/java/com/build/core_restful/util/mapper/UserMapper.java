package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserRequest userRequest);
}
