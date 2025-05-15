package com.build.core_restful.service;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UploadAvatarUser;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.UserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
    User getUserByEmail(String email);

    User getUserByEmailAndRefreshToken(String email, String refreshToken);

    boolean existUserByEmail(String email);

    void updateUserByEmail(String email, String refreshToken);

    PageResponse<Object> getAllUsers(Pageable pageable);

    UserResponse getUserById(Long id);

    UserResponse createUser(UserRequest newUser);

    UserResponse updateUser(Long id, UserRequest updateUser);

    UserResponse updateRoleUser(UpdateRoleUserRequest updateRoleUserRequest);

    boolean banUser(Long id);

    UserResponse updateAvatarUser(UploadAvatarUser uploadAvatarUser);

}
