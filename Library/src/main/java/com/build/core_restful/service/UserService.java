package com.build.core_restful.service;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UpdatePasswordUserRequest;
import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.util.enums.GenderEnum;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getUserByEmail(String email);

    User getUserByEmailAndRefreshToken(String email, String refreshToken);

    boolean existUserByEmail(String email);

    void updateUserByEmail(String email, String refreshToken);

    PageResponse<Object> getAllUsers(
        String keyword,
        GenderEnum gender,
        Long roleId,
        String userStatus,
        Pageable pageable        
    );

    UserResponse getUserById(Long id);

    UserResponse createUser(UserRequest newUser);

    UserResponse updateUser(Long id, UserRequest updateUser);

    UserResponse updateRoleUser(UpdateRoleUserRequest updateRoleUserRequest);

    boolean updatePasswordUser(UpdatePasswordUserRequest userRequest);
    
    Long getQuantityUser();

    boolean banUser(Long id);

    boolean restoreUser(Long id);

    void deleteUser(Long id);

    

}
