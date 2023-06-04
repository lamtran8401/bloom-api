package com.bloom.api.dto.product;

import com.bloom.api.models.Product;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductDTOMapper implements Function<Product, ProductDTO> {
    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getImages(),
            product.getPrice(),
            product.getCategory(),
            product.getBrand(),
            product.isVisible(),
            product
                .getProductDetails()
                .stream()
                .map(new ProductDetailDTOMapper())
                .toList(),
            product.getSoldOut(),
            product.isDeleted()
        );
    }
}
