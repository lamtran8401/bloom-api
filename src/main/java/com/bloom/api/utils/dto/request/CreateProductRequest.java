package com.bloom.api.utils.dto.request;

import com.bloom.api.models.Product;
import com.bloom.api.models.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    private List<MultipartFile> files;
    private Product product;
    private List<ProductDetail> productDetails;
}
