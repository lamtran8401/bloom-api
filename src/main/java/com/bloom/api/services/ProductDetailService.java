package com.bloom.api.services;

import com.bloom.api.repositories.ProductDetailRepository;
import com.bloom.api.utils.dto.request.ProductDetailsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;

    public void editProductDetail(Integer productDetailId, ProductDetailsRequest req) {
        productDetailRepository.findById(productDetailId).ifPresent(productDetail -> {
            productDetail.setColor(req.getColor());
            productDetail.setSize(req.getSize());
            productDetail.setStock(req.getStock());
            productDetail.setPrice(req.getPrice());
            productDetailRepository.save(productDetail);
        });
    }
}
