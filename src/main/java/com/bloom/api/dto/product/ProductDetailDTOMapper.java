package com.bloom.api.dto.product;

import com.bloom.api.models.ProductDetail;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductDetailDTOMapper implements Function<ProductDetail, ProductDetailDTO> {
    @Override
    public ProductDetailDTO apply(ProductDetail productDetail) {
        return new ProductDetailDTO(
            productDetail.getId(),
            productDetail.getColor(),
            productDetail.getSize(),
            productDetail.getStock(),
            productDetail.getPrice(),
            productDetail.isDeleted(),
            productDetail.getProduct().getId()
        );
    }
}
