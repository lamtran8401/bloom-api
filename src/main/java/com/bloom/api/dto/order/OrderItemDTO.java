package com.bloom.api.dto.order;

import com.bloom.api.dto.product.ProductDetailDTO;

public record OrderItemDTO(
    Integer id,
    ProductOrderDTO product,
    ProductDetailDTO detail,
    Integer quantity,
    Double price
) {
}
