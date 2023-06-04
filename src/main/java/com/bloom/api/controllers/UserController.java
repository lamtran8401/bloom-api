package com.bloom.api.controllers;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.services.UserService;
import com.bloom.api.utils.AuthContext;
import com.bloom.api.utils.dto.request.UpdateInfoRequest;
import com.bloom.api.utils.dto.request.UpdatePasswordRequest;
import com.bloom.api.utils.dto.response.ResponseHandler;
import com.bloom.api.utils.dto.response.ResponseSender;
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
    private final MappedDTO mappedDTO;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return ResponseEntity.ok(
            mappedDTO.mapUsersDTO(userService.getAll())
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(mappedDTO.mapUserDTO(userService.getById(id)));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateInfo(Principal principal, @RequestBody UpdateInfoRequest req) {
        var currentUserEmail = principal.getName();
        return ResponseEntity.ok(
            mappedDTO.mapUserDTO(userService.update(currentUserEmail, req))
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(Principal principal) {
        return ResponseEntity.ok(
            mappedDTO.mapUserDTO(userService.getByEmail(principal.getName()))
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseSender> changePassword(@RequestBody UpdatePasswordRequest req) {
        Integer currentUserId = AuthContext.getUserId();
        userService.updatePassword(currentUserId, req.getOldPassword(), req.getNewPassword());
        return ResponseEntity.ok(ResponseHandler.ok("Password changed successfully."));
    }
}
