package com.rc.tech.productservice.controller;


import com.rc.tech.productservice.dto.ProductRequest;
import com.rc.tech.productservice.dto.ProductResponse;
import com.rc.tech.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

   private final ProductService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProduct() {
        return service.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProducts(@RequestBody ProductRequest productRequest) {
        service.addProduct(productRequest);
    }
}
