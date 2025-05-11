package com.build.core_restful.controller.user;

import com.build.core_restful.domain.request.AddressRequest;
import com.build.core_restful.domain.request.SetAddressDefault;
import com.build.core_restful.domain.response.AddressResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.AddressService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/user/{id}")
    @AddMessage("Get addresses by user")
    public ResponseEntity<PageResponse<Object>> getAllByUser(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(addressService.getAllAddresses(id, pageable));
    }

    @GetMapping("/{id}")
    @AddMessage("Get addresses by id")
    public ResponseEntity<Object> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @PostMapping
    @AddMessage("Create address")
    public ResponseEntity<Object> createAddress(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.createAddress(request));
    }

    @PutMapping("/{id}")
    @AddMessage("Update address")
    public ResponseEntity<Object> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(id, request));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete address")
    public ResponseEntity<Object> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }

    @PutMapping
    @AddMessage("Set address default")
    public ResponseEntity<AddressResponse> setAddressDefault(@RequestBody SetAddressDefault addressDefault){
        return ResponseEntity.ok(addressService.setAddressDefault(addressDefault));
    }
}
