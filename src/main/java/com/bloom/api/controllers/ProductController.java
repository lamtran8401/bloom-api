package com.bloom.api.controllers;

import com.bloom.api.models.Product;
import com.bloom.api.services.ProductService;
import com.bloom.api.utils.dto.response.ResponseHandler;
import com.bloom.api.utils.dto.response.ResponseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Product> createProduct(@RequestPart MultipartFile[] files, @ModelAttribute Product product) {
        return ResponseEntity.ok(productService.create(product, files));
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
    public ResponseEntity<ResponseSender> deleteProductById(@PathVariable Integer productId) {
        productService.deleteById(productId);
        return ResponseEntity.ok(
                ResponseHandler.ok("Product deleted successfully"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/hide/{productId}")
    public ResponseEntity<ResponseSender> hideProductById(@PathVariable Integer productId) {
        productService.hideById(productId);
        return ResponseEntity.ok(
                ResponseHandler.ok("Product hidden successfully"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/show/{productId}")
    public ResponseEntity<ResponseSender> showProductById(@PathVariable Integer productId) {
        productService.showById(productId);
        return ResponseEntity.ok(
                ResponseHandler.ok("Product shown successfully"));
    }

}
