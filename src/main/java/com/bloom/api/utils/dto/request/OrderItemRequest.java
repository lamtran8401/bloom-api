package com.bloom.api.utils.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderItemRequest {
    private Integer productId;
    private Integer productDetailId;
    private Integer quantity;
}
