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
public class Product extends BaseModel {
    @Column(nullable = false)
    private String name;
    private String description;
    @ElementCollection
    private List<String> images;
    @Column(nullable = false)
    private Double price;
    private String category;
    private String brand;
    private boolean isVisible = true;
    @OneToMany(mappedBy = "product",
        orphanRemoval = true,
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    private List<ProductDetail> productDetails;
    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;

    public void setProductInfo(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.images = product.getImages();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.brand = product.getBrand();
    }

}
