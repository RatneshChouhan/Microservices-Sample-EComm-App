package com.rc.tech.productservice.service;

import com.rc.tech.productservice.dto.ProductRequest;
import com.rc.tech.productservice.dto.ProductResponse;
import com.rc.tech.productservice.model.Product;
import com.rc.tech.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        List<Product> products =  productRepository.findAll();
        return products.stream()
                .map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public void addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                        .name(productRequest.getName())
                        .description(productRequest.getDescription())
                        .price(productRequest.getPrice())
                        .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }
}
