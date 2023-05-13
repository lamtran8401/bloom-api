package com.bloom.api.controllers;

import com.bloom.api.models.Address;
import com.bloom.api.services.AddressService;
import com.bloom.api.utils.AuthContext;
import com.bloom.api.utils.responseDTO.RecordDeletedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddress() {
        return ResponseEntity.ok(addressService.getAllAddress(AuthContext.getUserId()));
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        var userId = AuthContext.getUserId();
        return ResponseEntity.ok(addressService.addAddress(userId, address));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<RecordDeletedResponse> deleteAddress(@PathVariable Integer addressId) {
        return ResponseEntity.ok(addressService.deleteAddress(addressId));
    }
}
