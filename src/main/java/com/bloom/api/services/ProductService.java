package com.bloom.api.services;

import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.models.Product;
import com.bloom.api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UploaderService uploaderService;

    public List<Product> getAll() {
        return productRepository.findByIsVisibleTrue();
    }

    @Transactional
    public Product create(Product p, MultipartFile[] files) {
        Product product = productRepository.save(p);
        List<String> urls = uploaderService.uploadImages(files, "p" + product.getId());
        product.setImages(urls);

        return product;
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).orElseThrow(()
                -> new RecordNotFoundException("Product not found with id: " + id));
    }

    public void deleteById(Integer id) {
        productRepository.findById(id).orElseThrow(()
                -> new RecordNotFoundException("Product not found with id: " + id));
        productRepository.deleteById(id);
        uploaderService.deleteImageFolder("p" + id);
    }

    public Product updateById(Integer productId, Product product) {
        Product productToUpdate = productRepository.findById(productId).orElseThrow(()
                -> new RecordNotFoundException("Product not found with id: " + productId));
        productToUpdate.setProductInfo(product);

        return productRepository.save(productToUpdate);
    }

    public void hideById(Integer productId) {
        productRepository.updateIsVisibleById(false, productId);
    }

    public void showById(Integer productId) {
        productRepository.updateIsVisibleById(true, productId);
    }
}
