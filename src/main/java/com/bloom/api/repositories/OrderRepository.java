package com.bloom.api.repositories;

import com.bloom.api.enums.OrderStatus;
import com.bloom.api.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserId(Integer userId);

    Optional<Order> findByIdAndUserId(Integer id, Integer userId);

    List<Order> findAllByUserIdOrderByCreatedAtDesc(Integer userId);

    List<Order> findAllByUserIdAndStatusOrderByCreatedAtDesc(Integer userId, OrderStatus status);

    @Query("select o from Order o order by o.createdAt DESC")
    List<Order> findByOrderByCreatedAtDesc();
}