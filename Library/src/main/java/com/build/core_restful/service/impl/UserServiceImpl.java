package com.build.core_restful.service.impl;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.repository.RoleRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.service.UserService;
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
    private final CloudinaryUpload cloudinaryUpload;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, CloudinaryUpload cloudinaryUpload, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.cloudinaryUpload = cloudinaryUpload;
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
        Page<User> page = userRepository.findByActive(true, pageable);
        page.map(userMapper::toUserResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .content(page.getContent())
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {
        if(!userRepository.existsById(id)){
            throw new NewException("User have id: " + id + " not exist!");
        }
        return userMapper.toUserResponse(userRepository.findByIdAndActive(id, true));
    }

    @Override
    public UserResponse createUser(UserRequest newUser) {
        if(userRepository.existsByEmail(newUser.getEmail())){
            throw new NewException("User have email: " + newUser.getEmail() + " exist! ");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(newUser)));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest updateUser) {
        if(!userRepository.existsById(id)){
            throw new NewException("User id: " + id + " not exist!");
        }

        User currentUser = userRepository.findByIdAndActive(id, true);

        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        userMapper.updateUser(currentUser, updateUser);

        return userMapper.toUserResponse(userRepository.save(currentUser));
    }

    @Override
    public UserResponse updateRoleUser(UpdateRoleUserRequest updateRoleUserRequest) {

        if(!userRepository.existsById(updateRoleUserRequest.getUser_id())){
            throw new NewException("User have id: " + updateRoleUserRequest.getUser_id() + " not exist!");
        }

        User currentUser = userRepository.findById(updateRoleUserRequest.getUser_id()).get();

        currentUser.setRole(roleRepository.findById(updateRoleUserRequest.getRole_id()).get());

        return userMapper.toUserResponse(userRepository.save(currentUser));
    }

    @Override
    public boolean banUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new NewException("User have id: " + id + " not exist!");
        }

        User currentUser = userRepository.findById(id).get();
        currentUser.setActive(false);

        try{
            userRepository.save(currentUser);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateAvatarUser(Long id, MultipartFile file) {
        if(!userRepository.existsById(id)){
            throw new NewException("User have id: " + id + " not exist!");
        }

        try{
            String avt = cloudinaryUpload.uploadFile(file);
            User currentUser = userRepository.findById(id).get();
            currentUser.setAvatar(avt);
            userRepository.save(currentUser);
        } catch (IOException e) {
            throw new NewException("Upload file fail");
        }

        return true;
    }
}
