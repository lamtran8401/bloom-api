package com.bloom.api.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record UserDTO(
    Integer id,
    String name,
    String email,
    String phone,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate birthDate,
    String gender,
    String role
) {
}
