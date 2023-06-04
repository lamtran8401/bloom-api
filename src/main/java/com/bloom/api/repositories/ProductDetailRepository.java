package com.bloom.api.repositories;

import com.bloom.api.models.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    @Transactional
    @Modifying
    @Query("update ProductDetail p set p.isDeleted = ?1 where p.id = ?2")
    void updateIsDeletedById(boolean isDeleted, Integer id);

    Optional<ProductDetail> findByIdAndProductId(Integer id, Integer productId);

}