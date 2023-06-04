package com.bloom.api.dto.order;

import com.bloom.api.dto.product.ProductDetailDTOMapper;
import com.bloom.api.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderItemDTOMapper implements Function<OrderItem, OrderItemDTO> {
    @Override
    public OrderItemDTO apply(OrderItem orderItem) {
        return new OrderItemDTO(
            orderItem.getId(),
            new ProductOrderDTOMapper().apply(orderItem.getProduct()),
            new ProductDetailDTOMapper().apply(orderItem.getProductDetail()),
            orderItem.getQuantity(),
            orderItem.getPrice()
        );
    }
}
