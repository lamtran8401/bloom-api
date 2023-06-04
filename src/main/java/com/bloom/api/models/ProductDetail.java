package com.bloom.api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDetail extends BaseModel {
    private String color;
    private String size;
    private int stock;
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @OneToMany(mappedBy = "productDetail",
        orphanRemoval = true,
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
    private boolean isDeleted = false;
}
