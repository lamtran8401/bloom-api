package com.bloom.api.controllers;

import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.services.UserService;
import com.bloom.api.utils.dto.request.UpdateInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
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
}
