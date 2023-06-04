package com.bloom.api.services;

import com.bloom.api.exception.RecordNotFoundException;
import com.bloom.api.models.Product;
import com.bloom.api.models.ProductDetail;
import com.bloom.api.repositories.ProductDetailRepository;
import com.bloom.api.repositories.ProductRepository;
import com.bloom.api.utils.dto.request.CreateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UploaderService uploaderService;
    private final ProductDetailRepository productDetailRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product create(CreateProductRequest req) {
        Product product = req.getProduct();
        product.setProductDetails(req.getProductDetails());
        List<ProductDetail> productDetails = product.getProductDetails();
        productDetails.forEach(productDetail -> {
            if (productDetail.getPrice() == null) productDetail.setPrice(product.getPrice());
            productDetail.setProduct(product);
        });
        Product newProduct = productRepository.save(product);
        List<String> urls = uploaderService.uploadImages(req.getFiles(), "p" + newProduct.getId());
        newProduct.setImages(urls);
        return newProduct;
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).orElseThrow(()
            -> new RecordNotFoundException("Product not found with id: " + id));
    }

    public void deleteById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(()
            -> new RecordNotFoundException("Product not found with id: " + id));
        product.setDeleted(true);
    }

    public Product updateById(Integer productId, Product product) {
        Product productToUpdate = productRepository.findById(productId).orElseThrow(()
            -> new RecordNotFoundException("Product not found with id: " + productId));
        productToUpdate.setProductInfo(product);

        return productRepository.save(productToUpdate);
    }

    public void setVisibleById(Integer productId, boolean isVisible) {
        productRepository.updateIsVisibleById(isVisible, productId);
    }

    public void setDeletedByDetailId(List<Integer> detailIds) {
        detailIds.forEach(detailId -> productDetailRepository.updateIsDeletedById(true, detailId));
    }
}
