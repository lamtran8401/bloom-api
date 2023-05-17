package com.bloom.api.controllers;

import com.bloom.api.models.Address;
import com.bloom.api.services.AddressService;
import com.bloom.api.utils.AuthContext;
import com.bloom.api.utils.dto.response.ResponseHandler;
import com.bloom.api.utils.dto.response.ResponseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
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

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId, AuthContext.getUserId()));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Integer addressId, @RequestBody Address address) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, address, AuthContext.getUserId()));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ResponseSender> deleteAddress(@PathVariable Integer addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(
                ResponseHandler.ok("Address deleted successfully")
        );
    }
}
