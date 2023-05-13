package com.bloom.api.controllers;

import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.models.Address;
import com.bloom.api.services.UserService;
import com.bloom.api.utils.AuthContext;
import com.bloom.api.utils.requestDTO.UpdateInfoRequest;
import com.bloom.api.utils.responseDTO.DeleteAddressResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateInfo(Principal principal, @RequestBody UpdateInfoRequest req) {
        var currentUserEmail = principal.getName();
        return ResponseEntity.ok(userService.update(currentUserEmail, req));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(Principal principal) {
        return ResponseEntity.ok(userService.getByEmail(principal.getName()));
    }

    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAllAddress() {
        return ResponseEntity.ok(userService.getAllAddress(AuthContext.getUserId()));
    }

    @PostMapping("/address")
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        var userId = AuthContext.getUserId();
        return ResponseEntity.ok(userService.addAddress(userId, address));
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@PathVariable Integer addressId) {
        return ResponseEntity.ok(userService.deleteAddress(addressId));
    }
}
