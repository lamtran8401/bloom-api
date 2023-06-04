package com.bloom.api.services;

import com.bloom.api.enums.OrderStatus;
import com.bloom.api.exception.BadRequestException;
import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.models.*;
import com.bloom.api.repositories.OrderRepository;
import com.bloom.api.repositories.ProductDetailRepository;
import com.bloom.api.repositories.ProductRepository;
import com.bloom.api.repositories.UserRepository;
import com.bloom.api.utils.dto.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    public Order createOrder(OrderRequest orderRequest, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RecordNotFoundException("User not found with id: " + userId)
        );

        Order order = new Order();
        order.setUser(user);
        order.setRecipientName(orderRequest.getRecipientName());
        order.setRecipientPhone(orderRequest.getRecipientPhone());
        order.setRecipientAddress(orderRequest.getRecipientAddress());

        orderRequest.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(
                () -> new RecordNotFoundException("Product not found with id: " + item.getProductId())
            );

            ProductDetail productDetail = productDetailRepository
                .findByIdAndProductId(item.getProductDetailId(), item.getProductId())
                .orElseThrow(
                    () -> new RecordNotFoundException("Product detail not found with id: " + item.getProductDetailId())
                );

            if (productDetail.getStock() < item.getQuantity())
                throw new BadRequestException("Product detail stock is not enough");
            productDetail.setStock(productDetail.getStock() - item.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductDetail(productDetail);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(productDetail.getPrice());
            order.getOrderItems().add(orderItem);
            order.setStatus(OrderStatus.PENDING);
        });

        order.setTotal(calculateTotalPrice(order));
        if (order.getTotal().compareTo(BigDecimal.valueOf(500000)) < 0)
            order.setTotal(order.getTotal().add(BigDecimal.valueOf(30000)));

        order.setTotalQuantity(calculateTotalQuantity(order));

        orderRepository.save(order);

        return order;
    }

    private BigDecimal calculateTotalPrice(Order order) {
        return order
            .getOrderItems()
            .stream()
            .map(orderItem -> BigDecimal.valueOf(orderItem.getPrice() * orderItem.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int calculateTotalQuantity(Order order) {
        return order
            .getOrderItems()
            .stream()
            .mapToInt(OrderItem::getQuantity)
            .sum();
    }

    public List<Order> getAllOrders(Integer userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public void cancelOrder(Integer id, Integer userId) {
        userRepository.findById(userId).orElseThrow(
            () -> new RecordNotFoundException("User not found with id: " + userId)
        );
        Order order = orderRepository.findByIdAndUserId(id, userId).orElseThrow(
            () -> new RecordNotFoundException("Order not found with id: " + id)
        );

        if (order.getStatus() != OrderStatus.PENDING)
            throw new BadRequestException("Order status is not pending. Cannot be canceled");

        order.setStatus(OrderStatus.CANCELED);
    }
}
