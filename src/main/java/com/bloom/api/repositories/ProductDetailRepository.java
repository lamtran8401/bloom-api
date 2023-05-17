package com.bloom.api.repositories;

import com.bloom.api.models.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    Optional<ProductDetail> findByIdAndProductId(Integer id, Integer productId);
}