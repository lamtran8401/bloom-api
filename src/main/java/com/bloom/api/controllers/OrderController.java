package com.bloom.api.controllers;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.dto.order.OrderDTO;
import com.bloom.api.enums.OrderStatus;
import com.bloom.api.services.OrderService;
import com.bloom.api.utils.AuthContext;
import com.bloom.api.utils.dto.request.OrderRequest;
import com.bloom.api.utils.dto.response.ResponseHandler;
import com.bloom.api.utils.dto.response.ResponseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MappedDTO mappedDTO;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(
            mappedDTO.mapOrdersDTO(
                orderService.getAllOrders(AuthContext.getUserId())
            )
        );
    }

    @GetMapping("/query")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByStatus(@RequestParam OrderStatus status) {
        return ResponseEntity.ok(
            mappedDTO.mapOrdersDTO(
                orderService.getAllByStatus(AuthContext.getUserId(), status)
            )
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin-orders")
    public ResponseEntity<List<OrderDTO>> getAllOrdersAdmin() {
        return ResponseEntity.ok(
            mappedDTO.mapOrdersDTO(
                orderService.getAll()
            )
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin-orders/{id}")
    public ResponseEntity<ResponseSender> updateOrderStatus(@PathVariable Integer id, @RequestParam OrderStatus status) {
        orderService.updateStatus(id, status);
        return ResponseEntity.ok(ResponseHandler
            .ok("Order status updated successfully"));
    }


    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest orderRequest) {
        Integer userId = AuthContext.getUserId();
        return ResponseEntity.ok(
            mappedDTO.mapOrderDTO(
                orderService.createOrder(orderRequest, userId)
            )
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ResponseSender> cancelOrder(@PathVariable Integer id) {
        Integer userId = AuthContext.getUserId();
        orderService.cancelOrder(id, userId);
        return ResponseEntity.ok(ResponseHandler
            .ok("Order canceled successfully"));
    }
}
