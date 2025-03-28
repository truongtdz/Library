package com.build.core_restful.service.impl;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UserCreateRequest;
import com.build.core_restful.domain.request.UserUpdateRequest;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
    public boolean existUserById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userMapper.toUserResponse(userRepository.findById(id).get());
    }

    @Override
    public UserResponse createUser(UserCreateRequest newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(newUser)));
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest updateUser) {
        User currentUser = userRepository.findById(updateUser.getId()).get();

        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        userMapper.updateUser(currentUser, updateUser);

        return userMapper.toUserResponse(userRepository.save(currentUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
