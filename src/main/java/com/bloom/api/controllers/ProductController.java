package com.bloom.api.controllers;

import com.bloom.api.dto.MappedDTO;
import com.bloom.api.dto.product.ProductDTO;
import com.bloom.api.models.Product;
import com.bloom.api.services.ProductService;
import com.bloom.api.utils.dto.request.CreateProductRequest;
import com.bloom.api.utils.dto.response.ResponseHandler;
import com.bloom.api.utils.dto.response.ResponseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final MappedDTO mappedDTO;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProduct() {
        return ResponseEntity.ok(
            mappedDTO.mapProductsDTO(productService.getAll()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductDTO> createProduct(
        @ModelAttribute CreateProductRequest req) {
        return ResponseEntity.ok(
            mappedDTO.mapProductDTO(productService.create(req))
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer productId, @RequestBody Product product) {
        return ResponseEntity.ok(
            mappedDTO.mapProductDTO(productService.updateById(productId, product))
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer productId) {
        return ResponseEntity.ok(
            mappedDTO.mapProductDTO(productService.getById(productId))
        );
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
        productService.setVisibleById(productId, false);
        return ResponseEntity.ok(
            ResponseHandler.ok("Product hidden successfully"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/show/{productId}")
    public ResponseEntity<ResponseSender> showProductById(@PathVariable Integer productId) {
        productService.setVisibleById(productId, true);
        return ResponseEntity.ok(
            ResponseHandler.ok("Product shown successfully"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/delete")
    public ResponseEntity<ResponseSender> deleteProducts(@RequestBody List<Integer> detailIds) {
        productService.setDeletedByDetailId(detailIds);
        return ResponseEntity.ok(
            ResponseHandler.ok("Products detail deleted successfully"));
    }

}
