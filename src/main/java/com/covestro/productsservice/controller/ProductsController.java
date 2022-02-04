package com.covestro.productsservice.controller;

import com.covestro.products.api.ProductsApi;
import com.covestro.products.api.model.FilteredProduct;
import com.covestro.products.api.model.Product;
import com.covestro.products.api.model.ProductReq;
import com.covestro.productsservice.service.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Controller
@Slf4j
@RequiredArgsConstructor
@Validated
public class ProductsController implements ProductsApi {

    ProductsService productsService;

    @Autowired
    ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<FilteredProduct> getAllProducts(
            @NotNull @Valid BigDecimal page,
            @NotNull @Valid BigDecimal pageSize,
            @NotNull @Valid String sort,
            @Valid String name,
            @Valid String category) {
        FilteredProduct products = productsService.getAllProducts(page, pageSize, sort, name, category);
        return ResponseEntity.ok(products);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> getProductsById(String productId) {
        Product product = productsService.getProductsById(productId);
        return ResponseEntity.ok(product);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> createProducts(ProductReq productReq) {
        Product product = productsService.createProduct(productReq);
        return ResponseEntity.ok(product);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> updateProductById(String productId, ProductReq productReq) {
        Product product = productsService.updateProduct(productId, productReq);
        return ResponseEntity.ok(product);
    }

}
