package com.covestro.productsservice.service.repository;

import com.covestro.productsservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ProductsRepository extends JpaRepository<Product, Serializable> {
}
