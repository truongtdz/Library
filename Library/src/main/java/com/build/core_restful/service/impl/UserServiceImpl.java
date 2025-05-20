package com.build.core_restful.service.impl;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UploadAvatarUser;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.repository.RoleRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.enums.UserStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.UserMapper;
import com.build.core_restful.util.upload.CloudinaryUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByEmailAndRefreshToken(String email, String refreshToken) {
        return userRepository.findByEmailAndRefreshToken(email, refreshToken);
    }

    @Override
    public boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updateUserByEmail(String email, String refreshToken) {
        User currentUser = userRepository.findByEmail(email);

        currentUser.setRefreshToken(refreshToken);

        userRepository.save(currentUser);
    }

    @Override
    public PageResponse<Object> getAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findByStatus(UserStatusEnum.Active, pageable);
        Page<UserResponse> pageResponse = page.map(userMapper::toUserResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {
        if(!userRepository.existsById(id)){
            throw new NewException("User have id: " + id + " not exist!");
        }
        return userMapper.toUserResponse(userRepository.findByIdAndStatus(id, UserStatusEnum.Active));
    }

    @Override
    public UserResponse createUser(UserRequest newUser) {
        if(userRepository.existsByEmail(newUser.getEmail())) {
            throw new NewException("User have email: " + newUser.getEmail() + " exist! ");
        }

        User user = userMapper.toUser(newUser);

        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setStatus(UserStatusEnum.Active);
        user.setRole(roleRepository.findByName("USER"));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest updateUser) {
        if(!userRepository.existsById(id)){
            throw new NewException("User id: " + id + " not exist!");
        }

        User currentUser = userRepository.findByIdAndStatus(id, UserStatusEnum.Active);

        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        userMapper.updateUser(currentUser, updateUser);

        currentUser.setStatus(UserStatusEnum.Active);
        currentUser.setRole(roleRepository.findByName("USER"));

        return userMapper.toUserResponse(userRepository.save(currentUser));
    }

    @Override
    public UserResponse updateRoleUser(UpdateRoleUserRequest updateRoleUserRequest) {

        User currentUser = userRepository.findById(updateRoleUserRequest.getUserId())
                .orElseThrow(() -> new NewException("User have id: " + updateRoleUserRequest.getUserId() + " not exist!"));

        currentUser.setRole(
                roleRepository.findById(updateRoleUserRequest.getRoleId())
                        .orElseThrow ( () ->new NewException("Role have id: " + updateRoleUserRequest.getRoleId() + " not exist!"))
        );

        return userMapper.toUserResponse(userRepository.save(currentUser));
    }

    @Override
    public boolean banUser(Long id) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new NewException("User have id: " + id + " not exist!"));

        currentUser.setStatus(UserStatusEnum.Banned);

        try{
            userRepository.save(currentUser);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public UserResponse updateAvatarUser(UploadAvatarUser uploadAvatarUser) {
        User currentUser = userRepository.findByIdAndStatus(uploadAvatarUser.getUserId(), UserStatusEnum.Active);

        currentUser.setAvatar(uploadAvatarUser.getAvtUrl());

        return userMapper.toUserResponse(userRepository.save(currentUser));
    }
}
