package com.build.core_restful.service;

import com.build.core_restful.domain.request.AddressRequest;
import com.build.core_restful.domain.request.SetAddressDefault;
import com.build.core_restful.domain.response.AddressResponse;
import com.build.core_restful.domain.response.PageResponse;

import org.springframework.data.domain.Pageable;

public interface AddressService {
    PageResponse<Object> getAllAddresses(
        String city, 
        String district, 
        String ward, 
        String street, 
        String isDefault,
        Pageable pageable
    );

    PageResponse<Object> getAddressByUser(Long id, Pageable pageable);

    AddressResponse getAddressById(Long id);

    AddressResponse createAddress(AddressRequest request);

    AddressResponse updateAddress(Long id, AddressRequest request);

    boolean deleteAddress(Long id);

    AddressResponse setAddressDefault(SetAddressDefault addressDefault);

}
