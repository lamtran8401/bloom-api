package com.bloom.api.dto.address;

import java.time.Instant;

public record AddressDTO(
    Integer id,
    String receiver,
    String phone,
    Integer province,
    Integer district,
    Integer ward,
    String detail,
    boolean isDefault,
    Integer userId,
    Instant createdAt,
    Instant updatedAt
) {

}
