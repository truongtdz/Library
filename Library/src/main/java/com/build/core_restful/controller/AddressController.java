package com.build.core_restful.controller;

import com.build.core_restful.domain.request.AddressRequest;
import com.build.core_restful.domain.request.SetAddressDefault;
import com.build.core_restful.domain.response.AddressResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.AddressService;
import com.build.core_restful.util.annotation.AddMessage;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/all")
    @AddMessage("Get all addresses")
    public ResponseEntity<PageResponse<Object>> getAllAddress(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String district,
        @RequestParam(required = false) String ward,
        @RequestParam(required = false) String street,
        @RequestParam(required = false) String isDefault
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(addressService.getAllAddresses(
            city, district, ward, street, isDefault, pageable
        ));
    }

    @GetMapping("/by/user/{id}")
    @AddMessage("Get addresses by user")
    public ResponseEntity<PageResponse<Object>> getAllByUser(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(addressService.getAddressByUser(id, pageable));
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get addresses by id")
    public ResponseEntity<Object> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @PostMapping("/create")
    @AddMessage("Create address")
    public ResponseEntity<Object> createAddress(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.createAddress(request));
    }

    @PutMapping("/update{id}")
    @AddMessage("Update address")
    public ResponseEntity<Object> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(id, request));
    }

    @PutMapping("/update/default")
    @AddMessage("Set address default")
    public ResponseEntity<AddressResponse> setAddressDefault(@RequestBody SetAddressDefault addressDefault){
        return ResponseEntity.ok(addressService.setAddressDefault(addressDefault));
    }

    @DeleteMapping("/delete/{id}")
    @AddMessage("Delete address")
    public ResponseEntity<Object> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }

    
}
