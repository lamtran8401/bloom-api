package com.bloom.api.controllers;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.dto.address.AddressDTO;
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
    private final MappedDTO mappedDTO;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddress() {
        return ResponseEntity.ok(
            mappedDTO.mapAddressesDTO(
                addressService.getAllAddress(AuthContext.getUserId()))
        );
    }

    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(@RequestBody Address address) {
        var userId = AuthContext.getUserId();
        return ResponseEntity.ok(
            mappedDTO.mapAddressDTO(addressService.addAddress(userId, address))
        );
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Integer addressId) {
        return ResponseEntity.ok(
            mappedDTO.mapAddressDTO(addressService.getAddressById(addressId, AuthContext.getUserId()))
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer addressId, @RequestBody Address address) {
        return ResponseEntity.ok(
            mappedDTO.mapAddressDTO(addressService.updateAddress(addressId, address, AuthContext.getUserId()))
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ResponseSender> deleteAddress(@PathVariable Integer addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(
            ResponseHandler.ok("Address deleted successfully")
        );
    }
}
