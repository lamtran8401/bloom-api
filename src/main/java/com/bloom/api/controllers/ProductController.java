package com.bloom.api.controllers;

import com.bloom.api.models.Product;
import com.bloom.api.services.ProductService;
import com.bloom.api.utils.responseDTO.RecordDeletedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productService.getAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.create(product));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateById(productId, product));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<RecordDeletedResponse> deleteProductById(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.deleteById(productId));
    }
}
