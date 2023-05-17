package com.bloom.api.models;

import com.bloom.api.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order extends BaseModel {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
    @OneToOne
    @Getter(AccessLevel.NONE)
    private User user;
}
