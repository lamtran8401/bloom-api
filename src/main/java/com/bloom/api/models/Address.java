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
public class Address extends BaseModel {
    private String receiver;
    private String phone;
    private Integer province;
    private Integer district;
    private Integer ward;
    private String detail;
    private boolean isDefault = false;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void setAddress(Address address) {
        this.receiver = address.getReceiver();
        this.province = address.getProvince();
        this.district = address.getDistrict();
        this.ward = address.getWard();
        this.detail = address.getDetail();
        this.isDefault = address.isDefault();
    }
}
