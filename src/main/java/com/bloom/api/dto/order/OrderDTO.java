package com.bloom.api.dto.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDTO(
    Integer id,
    Integer userId,
    List<OrderItemDTO> orderItems,
    BigDecimal total,
    Integer totalQuantity,
    String status,
    String recipientName,
    String recipientPhone,
    String recipientAddress,
    Instant createdAt,
    Instant updatedAt
) {
}
