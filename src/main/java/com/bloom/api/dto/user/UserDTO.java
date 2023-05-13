package com.bloom.api.dto.user;

import com.bloom.api.models.Address;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record UserDTO(
    Integer id,
    String name,
    String email,
    String phone,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate birthDate,
    String gender,
    String role,
    List<Address> addresses
) {
}
