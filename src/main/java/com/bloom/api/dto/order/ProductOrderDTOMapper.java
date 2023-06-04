package com.bloom.api.dto.order;

import com.bloom.api.models.Product;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductOrderDTOMapper implements Function<Product, ProductOrderDTO> {
    @Override
    public ProductOrderDTO apply(Product product) {
        return new ProductOrderDTO(
            product.getId(),
            product.getName(),
            product.getImages(),
            product.getPrice(),
            product.getCategory(),
            product.getBrand()
        );
    }
}
