package com.covestro.productsservice.controller;

import com.covestro.products.api.model.CategoryEnum;
import com.covestro.products.api.model.FilteredProduct;
import com.covestro.products.api.model.Product;
import com.covestro.products.api.model.ProductReq;
import com.covestro.productsservice.service.ProductsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductsControllerTest {

    private ProductsService productsService;
    private ProductsController productsController;

    private final BigDecimal page = BigDecimal.valueOf(0);
    private final BigDecimal pageSize = BigDecimal.valueOf(5);
    private final String name = "Desmodur";
    private final String id = "33da0b01-d5eb-400a-a418-10d983c6f143";

    @BeforeEach
    void setup() {
        productsService = mock(ProductsService.class);
        productsController = new ProductsController(productsService);
    }

    @Test
    void testGetAllProducts_success() {
        FilteredProduct filteredProduct = new FilteredProduct();
        filteredProduct.addProductsItem(getProduct());
        when(this.productsService.getAllProducts(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(filteredProduct);
        ResponseEntity<FilteredProduct> result = productsController.getAllProducts(page, pageSize, "name", "", "");
        assertNotNull(ResponseEntity.ok(result));
        Assertions.assertEquals(1, Objects.requireNonNull(result.getBody().getProducts()).size());
    }

    @Test
    void testGetProductsById_success() {
        when(this.productsService.getProductsById(Mockito.anyString()))
                .thenReturn(getProduct());
        ResponseEntity<Product> result = productsController.getProductsById(id);
        assertNotNull(ResponseEntity.ok(result));
        Assertions.assertEquals(name, Objects.requireNonNull(result.getBody().getName()));
    }

    @Test
    void testUpdateProductById_success() {
        when(this.productsService.updateProduct(Mockito.anyString(), Mockito.any(ProductReq.class)))
                .thenReturn(getProduct());
        ResponseEntity<Product> result = productsController.updateProductById(id, getProductReq());
        assertNotNull(ResponseEntity.ok(result));
    }

    @Test
    void testCreateProduct_success() {
        when(this.productsService.createProduct(Mockito.any(ProductReq.class)))
                .thenReturn(getProduct());
        ResponseEntity<Product> result = productsController.createProducts(getProductReq());
        assertNotNull(ResponseEntity.ok(result));
    }

    private Product getProduct() {
        Product product = new Product();
        product.setName(name);
        product.setCategory(CategoryEnum.PUR);
        product.setCurrency("EUR");
        product.setPrice(BigDecimal.valueOf(10.23));
        return product;
    }

    private ProductReq getProductReq() {
        ProductReq product = new ProductReq();
        product.setName(name);
        product.setCategory(CategoryEnum.PUR);
        product.setCurrency("EUR");
        product.setCurrentPrice(BigDecimal.valueOf(10.23));
        return product;
    }
}
