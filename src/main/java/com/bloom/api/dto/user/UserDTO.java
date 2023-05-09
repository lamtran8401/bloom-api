package com.bloom.api.dto.user;

import java.time.LocalDate;

public record UserDTO(
    Integer id,
    String name,
    String email,
    String phone,
    LocalDate birthDate,
    String gender,
    String role
) {
}
