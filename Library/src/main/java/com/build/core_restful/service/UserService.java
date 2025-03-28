package com.build.core_restful.service;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UserCreateRequest;
import com.build.core_restful.domain.request.UserUpdateRequest;
import com.build.core_restful.domain.response.UserResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {
    User getUserByEmail(String email);

    User getUserByEmailAndRefreshToken(String email, String refreshToken);

    boolean existUserByEmail(String email);

    void updateUserByEmail(String email, String refreshToken);

    boolean existUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse createUser(UserCreateRequest user);

    UserResponse updateUser(UserUpdateRequest updateUser);

    void deleteUser(Long id);
}
