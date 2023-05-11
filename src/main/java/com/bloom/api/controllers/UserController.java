package com.bloom.api.controllers;

import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.services.UserService;
import com.bloom.api.utils.requestDTO.UpdateInfoRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostAuthorize("returnObject.body.email().equals(principal.username)")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateInfo(Principal principal, @RequestBody UpdateInfoRequest req) {
        var currentUserEmail = principal.getName();
        return ResponseEntity.ok(userService.updateUser(currentUserEmail, req));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(Principal principal) {
        return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
    }
}
