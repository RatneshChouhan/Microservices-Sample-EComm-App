package com.rc.tech.productservice.repository;

/*
import com.rc.tech.productservice.model.Product;
*/

import org.springframework.data.jpa.repository.JpaRepository;

import com.rc.tech.productservice.model.Product;

public interface ProductRepository extends JpaRepository <Product, Long> {
}
