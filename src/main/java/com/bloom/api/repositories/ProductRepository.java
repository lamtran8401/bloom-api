package com.bloom.api.repositories;

import com.bloom.api.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Transactional
    @Modifying
    @Query("update Product p set p.isVisible = ?1 where p.id = ?2")
    int updateIsVisibleById(boolean isVisible, Integer id);

    List<Product> findByIsVisibleAndIsDeleted(boolean isVisible, boolean isDeleted);
}