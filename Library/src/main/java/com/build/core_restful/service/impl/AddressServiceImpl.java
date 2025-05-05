package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Address;
import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.AddressRequest;
import com.build.core_restful.domain.response.AddressResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.AddressRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.service.AddressService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.AddressMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository,
                              UserRepository userRepository,
                              AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public PageResponse<Object> getAllAddresses(Long id, Pageable pageable) {
        Page<Address> page = addressRepository.findByUserId(id, pageable);
        Page<AddressResponse> pageResponse = page.map(addressMapper::toAddressResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public AddressResponse getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NewException("Address with id: " + id + " not found"));
        return addressMapper.toAddressResponse(address);
    }

    @Override
    public AddressResponse createAddress(AddressRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NewException("User with id: " + request.getUserId() + " not found"));

        Address address = addressMapper.toAddress(request);
        address.setUser(user);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @Override
    public AddressResponse updateAddress(Long id, AddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NewException("Address with id: " + id + " not found"));

        addressMapper.updateAddress(address, request);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @Override
    public boolean deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new NewException("Address with id: " + id + " does not exist!");
        }
        addressRepository.deleteById(id);
        return true;
    }
}
