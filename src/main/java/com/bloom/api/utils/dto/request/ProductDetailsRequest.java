package com.bloom.api.utils.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetailsRequest {
    private String color;
    private String size;
    private int stock;
    private Double price;
}
