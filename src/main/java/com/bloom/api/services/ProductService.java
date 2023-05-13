package com.bloom.api.services;

import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.models.Product;
import com.bloom.api.repositories.ProductRepository;
import com.bloom.api.utils.responseDTO.RecordDeletedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product getById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(()
            -> new RecordNotFoundException("Product not found with id: " + id));
        product.getProductDetails();
        return product;
    }

    public RecordDeletedResponse deleteById(Integer id) {
        productRepository.findById(id).orElseThrow(()
            -> new RecordNotFoundException("Product not found with id: " + id));
        productRepository.deleteById(id);
        return RecordDeletedResponse.builder()
            .message("Product deleted successfully.")
            .statusCode(HttpStatus.OK.value())
            .build();
    }

    public Product updateById(Integer productId, Product product) {
        Product productToUpdate = productRepository.findById(productId).orElseThrow(()
            -> new RecordNotFoundException("Product not found with id: " + productId));
        if (product.getName() != null) productToUpdate.setName(product.getName());
        if (product.getDescription() != null)
            productToUpdate.setDescription(product.getDescription());
        if (product.getPrice() != null) productToUpdate.setPrice(product.getPrice());
        if (product.getCategory() != null) productToUpdate.setCategory(product.getCategory());
        if (product.getBrand() != null) productToUpdate.setBrand(product.getBrand());
        if (product.getImages() != null) productToUpdate.setImages(product.getImages());
        
        return productRepository.save(productToUpdate);
    }
}