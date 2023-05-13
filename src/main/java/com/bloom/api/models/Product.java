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
public class Product extends Base {
    private String name;
    private String description;
    @ElementCollection
    private List<String> images;
    private Double price;
    private String category;
    private String brand;
    @OneToMany(mappedBy = "product",
        orphanRemoval = true,
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    private List<ProductDetail> productDetails;
    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;

}
