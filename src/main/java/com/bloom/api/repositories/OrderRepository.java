package com.bloom.api.repositories;

import com.bloom.api.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserId(Integer userId);

    Optional<Order> findByIdAndUserId(Integer id, Integer userId);
}