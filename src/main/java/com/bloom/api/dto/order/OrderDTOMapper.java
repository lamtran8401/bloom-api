package com.bloom.api.dto.order;

import com.bloom.api.models.Order;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderDTOMapper implements Function<Order, OrderDTO> {

    @Override
    public OrderDTO apply(Order order) {
        return new OrderDTO(
            order.getId(),
            order.getUser().getId(),
            order
                .getOrderItems()
                .stream()
                .map(new OrderItemDTOMapper())
                .toList(),
            order.getTotal(),
            order.getTotalQuantity(),
            order.getStatus().name(),
            order.getRecipientName(),
            order.getRecipientPhone(),
            order.getRecipientAddress(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
}
