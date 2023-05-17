package com.bloom.api.controllers;

import com.bloom.api.models.Order;
import com.bloom.api.services.OrderService;
import com.bloom.api.utils.AuthContext;
import com.bloom.api.utils.dto.request.OrderRequest;
import com.bloom.api.utils.dto.response.ResponseHandler;
import com.bloom.api.utils.dto.response.ResponseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders(AuthContext.getUserId());
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest) {
        Integer userId = AuthContext.getUserId();
        return orderService.createOrder(orderRequest, userId);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ResponseSender> cancelOrder(@PathVariable Integer id) {
        Integer userId = AuthContext.getUserId();
        orderService.cancelOrder(id, userId);
        return ResponseEntity.ok(ResponseHandler
            .ok("Order canceled successfully"));
    }
}
