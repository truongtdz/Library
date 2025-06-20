package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Address;
import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.AddressRequest;
import com.build.core_restful.domain.request.SetAddressDefault;
import com.build.core_restful.domain.response.AddressResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.AddressRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.repository.specification.AddressSpecification;
import com.build.core_restful.service.AddressService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.AddressMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public PageResponse<Object> getAddressByUser(Long id, Pageable pageable) {
        if(!userRepository.existsById(id)){
            throw new NewException("User have id: " + id + " not exist!");
        }

        Page<Address> page = addressRepository.findByUserId(id, pageable);
        Page<AddressResponse> pageResponse = page.map(addressMapper::toAddressResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
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

    @Override
    public AddressResponse setAddressDefault(SetAddressDefault addressDefault) {
        if(!userRepository.existsById(addressDefault.getUserId())){
            throw new NewException("User have id: " + addressDefault.getUserId() + " not exist!");
        }

        List<Address> addressList = addressRepository.findByUserId(addressDefault.getUserId());

        addressList.forEach(address -> address.setIsDefault("false"));
        addressRepository.saveAll(addressList);

        Address address = addressRepository.findByIdAndUserId(addressDefault.getAddressId(), addressDefault.getUserId());
        address.setIsDefault("true");

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @Override
    public PageResponse<Object> getAllAddresses(
        String city, 
        String district, 
        String ward, 
        String street, 
        String isDefault,
        Pageable pageable
    ) {
        Specification<Address> spec = AddressSpecification.filterAddresses(city, district, ward, street, isDefault);

        Page<Address> page = addressRepository.findAll(spec, pageable);
        Page<AddressResponse> pageResponse = page.map(addressMapper::toAddressResponse);

        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

}
