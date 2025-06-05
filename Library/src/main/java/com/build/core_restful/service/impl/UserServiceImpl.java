package com.build.core_restful.service.impl;

import com.build.core_restful.domain.RentalOrder;
import com.build.core_restful.domain.RentedOrder;
import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.UpdatePasswordUserRequest;
import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.repository.RentalOrderRepository;
import com.build.core_restful.repository.RentedOrderRepository;
import com.build.core_restful.repository.RoleRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.repository.specification.UserSpecification;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.enums.EntityStatusEnum;
import com.build.core_restful.util.enums.GenderEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.UserMapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RentalOrderRepository rentalOrderRepository;
    private final RentedOrderRepository rentedOrderRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
        UserRepository userRepository, 
        RoleRepository roleRepository,
        UserMapper userMapper, 
        PasswordEncoder passwordEncoder,
        RentalOrderRepository rentalOrderRepository,
        RentedOrderRepository rentedOrderRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentedOrderRepository = rentedOrderRepository;
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
        String userStatus,
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
        return userMapper.toUserResponse(
            userRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                        .orElseThrow(() -> new NewException("User have id: " + id + " not exist!"))
        );
    }

    @Override
    public UserResponse createUser(UserRequest newUser) {
        if(userRepository.existsByEmail(newUser.getEmail())) {
            throw new NewException("User have email: " + newUser.getEmail() + " exist! ");
        }

        User user = userMapper.toUser(newUser);

        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setStatus(EntityStatusEnum.Active.toString());
        user.setRole(roleRepository.findByName("USER"));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest updateUser) {
        User currentUser = userRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                        .orElseThrow(() -> new NewException("User have id: " + id + " not exist!"));

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
    public boolean updatePasswordUser(UpdatePasswordUserRequest userRequest) {
        User currentUser = userRepository.findByIdAndStatus(userRequest.getUserId(), EntityStatusEnum.Active.toString())
                        .orElseThrow(() -> new NewException("User have id: " + userRequest.getUserId() + " not exist!"));

        if(!passwordEncoder.matches(userRequest.getCurrentPassword(), currentUser.getPassword())){
            throw new NewException("Current password is false");
        }

        currentUser.setPassword(passwordEncoder.encode(userRequest.getNewPassword()));

        userRepository.save(currentUser);
        return true;
    }

    @Override
    public Long getQuantityUserActive(){
        return userRepository.countByStatus(EntityStatusEnum.Active.toString());
    }

    @Override
    public Long getQuantityUserDelete(){
        return userRepository.countByStatus(EntityStatusEnum.Delete.toString());
    }

    @Override
    public boolean softDeleteUsers(List<Long> usersId) {
        try {
            for(Long id : usersId){
                User user = userRepository.findByIdAndStatus(id, EntityStatusEnum.Delete.toString())
                    .orElseThrow(() -> new NewException("User with id: " + id + " not found"));
                    
                user.setStatus(EntityStatusEnum.Delete.toString());

                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            throw new NewException("Error restoring users: " + e.getMessage());
        }
    }

    @Override
    public boolean restoreUsers(List<Long> usersId){
        try {
            for(Long id : usersId){
                User user = userRepository.findByIdAndStatus(id, EntityStatusEnum.Delete.toString())
                    .orElseThrow(() -> new NewException("User with id: " + id + " not found"));
                    
                user.setStatus(EntityStatusEnum.Active.toString());

                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            throw new NewException("Error restoring users: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Long id){
        List<RentalOrder> rentalOrder = rentalOrderRepository.findAllByUserId(id);
        rentalOrder.forEach(item -> item.setUser(null));
        rentalOrderRepository.saveAll(rentalOrder);

        List<RentedOrder> rentedOrder = rentedOrderRepository.findAllByUserId(id);
        rentedOrder.forEach(item -> item.setUser(null));
        rentedOrderRepository.saveAll(rentedOrder);

        userRepository.deleteById(id);
    }

}
