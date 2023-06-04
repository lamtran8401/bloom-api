package com.bloom.api.dto.order;

import java.util.List;

public record ProductOrderDTO(
    Integer id,
    String name,
    List<String> images,
    Double price,
    String category,
    String brand
) {
}
