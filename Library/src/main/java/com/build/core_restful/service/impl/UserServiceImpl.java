package com.build.core_restful.service.impl;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UpdatePasswordUserRequest;
import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UploadAvatar;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.repository.RoleRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.repository.specification.UserSpecification;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.enums.GenderEnum;
import com.build.core_restful.util.enums.UserStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public PageResponse<Object> getAllUsers(
        String keyword,
        GenderEnum gender,
        Long roleId,
        UserStatusEnum userStatus,
        Pageable pageable   
    ){
        Specification<User> spec = UserSpecification.filterUsers(keyword, gender, roleId, userStatus);
        Page<User> page = userRepository.findAll(spec, pageable);
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
        return userMapper.toUserResponse(userRepository.findByIdAndStatus(id, UserStatusEnum.Active.toString()));
    }

    @Override
    public UserResponse createUser(UserRequest newUser) {
        if(userRepository.existsByEmail(newUser.getEmail())) {
            throw new NewException("User have email: " + newUser.getEmail() + " exist! ");
        }

        User user = userMapper.toUser(newUser);

        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setStatus(UserStatusEnum.Active.toString());
        user.setRole(roleRepository.findByName("USER"));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest updateUser) {
        if(!userRepository.existsById(id)){
            throw new NewException("User id: " + id + " not exist!");
        }

        User currentUser = userRepository.findByIdAndStatus(id, UserStatusEnum.Active.toString());

        userMapper.updateUser(currentUser, updateUser);

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

        currentUser.setStatus(UserStatusEnum.Banned.toString());

        try{
            userRepository.save(currentUser);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public UserResponse updateAvatarUser(UploadAvatar uploadAvatar) {
        User currentUser = userRepository.findByIdAndStatus(uploadAvatar.getId(), UserStatusEnum.Active.toString());

        currentUser.setAvatar(uploadAvatar.getAvtUrl());

        return userMapper.toUserResponse(userRepository.save(currentUser));
    }

    @Override
    public boolean updatePasswordUser(UpdatePasswordUserRequest userRequest) {
        User currentUser = userRepository.findByIdAndStatus(userRequest.getUserId(), UserStatusEnum.Active.toString());

        if(!passwordEncoder.matches(userRequest.getCurrentPassword(), currentUser.getPassword())){
            throw new NewException("Current password is false");
        }

        currentUser.setPassword(passwordEncoder.encode(userRequest.getNewPassword()));

        userRepository.save(currentUser);
        return true;
    }
}
