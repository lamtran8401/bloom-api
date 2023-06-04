package com.bloom.api.dto.product;

public record ProductDetailDTO(
    Integer id,
    String color,
    String size,
    int stock,
    Double price,
    boolean isDeleted,
    Integer productId) {
}
