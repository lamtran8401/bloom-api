package com.bloom.api.utils.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class OrderRequest {
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
    private List<OrderItemRequest> items;
}
