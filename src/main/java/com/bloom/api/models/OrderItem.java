package com.bloom.api.models;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItem extends BaseModel {
    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDetail productDetail;
    private int quantity;
    private Double price;
}
