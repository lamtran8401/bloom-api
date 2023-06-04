package com.bloom.api.dto.product;

import java.util.List;

public record ProductDTO(
    Integer id,
    String name,
    String description,
    List<String> images,
    Double price,
    String category,
    String brand,
    boolean isVisible,
    List<ProductDetailDTO> productDetails,
    int soldOut,
    boolean isDeleted) {
}
