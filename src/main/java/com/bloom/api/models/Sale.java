package com.bloom.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Sale extends Base {
    private double discount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "sale", fetch = FetchType.LAZY)
    private Set<Product> products;

}
