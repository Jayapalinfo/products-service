package com.covestro.productsservice.service;

import com.covestro.products.api.model.CategoryEnum;
import com.covestro.products.api.model.Error;
import com.covestro.products.api.model.FilteredProduct;
import com.covestro.products.api.model.Product;
import com.covestro.products.api.model.ProductReq;
import com.covestro.productsservice.domain.Category;
import com.covestro.productsservice.exception.ProductsRuntimeException;
import com.covestro.productsservice.mapper.ProductsApiModelFormMapper;
import com.covestro.productsservice.service.repository.ProductsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class ProductsServiceTest {
    private ProductsRepository productsRepository;
    private ProductsApiModelFormMapper productsApiModelFormMapper;
    private ProductsService productsService;

    private final BigDecimal page = BigDecimal.valueOf(0);
    private final BigDecimal pageSize = BigDecimal.valueOf(5);
    private final String sort = "name";
    private final String name = "Desmodur";
    private final String id = "33da0b01-d5eb-400a-a418-10d983c6f143";

    @BeforeEach
    void setup() {
        productsRepository = mock(ProductsRepository.class);
        productsApiModelFormMapper = mock(ProductsApiModelFormMapper.class);
        productsService = new ProductsService(productsRepository, productsApiModelFormMapper);
    }

    @Test
    void testGetAllProducts_success() {
        PageImpl<com.covestro.productsservice.domain.Product> productPage = new PageImpl<>(List.of(getProductEntity()));
        FilteredProduct filteredProduct = new FilteredProduct();
        filteredProduct.addProductsItem(getProduct());
        Mockito.when(this.productsRepository.findAll(Mockito.any(Example.class), Mockito.any(Pageable.class))).thenReturn(productPage);
        Mockito.when(this.productsApiModelFormMapper.toFilteredProduct(Mockito.any(Page.class))).thenReturn(filteredProduct);
        FilteredProduct result = productsService.getAllProducts(page, pageSize, sort, "", "");
        assertNotNull(result);
        Assertions.assertEquals(1, Objects.requireNonNull(result.getProducts()).size());
    }

    @Test
    void testGetProductsById_success() {
        Mockito.when(this.productsRepository.findById(Mockito.anyString())).thenReturn(Optional.of(getProductEntity()));
        Mockito.when(this.productsApiModelFormMapper.toProductResponse(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProduct());
        Product result = productsService.getProductsById(id);
        assertNotNull(result);
        Assertions.assertEquals(name, Objects.requireNonNull(result.getName()));
    }

    @Test
    void testGetProductsById_NotFound() {
        Mockito.when(this.productsRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
        Mockito.when(this.productsApiModelFormMapper.toProductResponse(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProduct());
        ProductsRuntimeException exception = Assertions.assertThrows(ProductsRuntimeException.class, () -> {
            productsService.getProductsById(id);
        });
        Assertions.assertEquals("404",exception.getErrors().stream().map(Error::getCode).findFirst().orElse(null));
    }

    @Test
    void testUpdateProductById_success() {
        Mockito.when(this.productsRepository.findById(Mockito.anyString())).thenReturn(Optional.of(getProductEntity()));
        Mockito.when(this.productsApiModelFormMapper.toProductResponse(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProduct());
        Mockito.doNothing().when(this.productsApiModelFormMapper).updateProduct(Mockito.any(ProductReq.class), Mockito.any(com.covestro.productsservice.domain.Product.class));
        Mockito.when(this.productsApiModelFormMapper.toProductResponse(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProduct());
        Mockito.when(this.productsRepository.save(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProductEntity());
        Product result = productsService.updateProduct(id, getProductReq());
        assertNotNull(result);
    }

    @Test
    void testCreateProduct_success() {
        Mockito.when(this.productsRepository.save(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProductEntity());
        Mockito.when(this.productsApiModelFormMapper.toProduct(Mockito.any(ProductReq.class))).thenReturn(getProductEntity());
        Mockito.when(this.productsApiModelFormMapper.toProductResponse(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProduct());
        Product result = productsService.createProduct(getProductReq());
        assertNotNull(ResponseEntity.ok(result));
    }

    @Test
    void testCreateProducts_success() {
        Mockito.when(this.productsRepository.save(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProductEntity());
        Mockito.when(this.productsApiModelFormMapper.toProduct(Mockito.any(ProductReq.class))).thenReturn(getProductEntity());
        Mockito.when(this.productsApiModelFormMapper.toProductResponse(Mockito.any(com.covestro.productsservice.domain.Product.class))).thenReturn(getProduct());
        Iterable<com.covestro.productsservice.domain.Product> result = productsService.saveAll(List.of(getProductEntity()));
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

    private com.covestro.productsservice.domain.Product getProductEntity() {
        com.covestro.productsservice.domain.Product product = new com.covestro.productsservice.domain.Product();
        product.setName(name);
        product.setCategory(Category.PUR);
        product.setCurrency("EUR");
        product.setCurrentPrice("10.23");
        return product;
    }

    private List<com.covestro.products.api.model.Error> notFound() {
        List<com.covestro.products.api.model.Error> errors = new ArrayList<>();
        com.covestro.products.api.model.Error error = new Error();
        error.setCode("404");
        error.setMessage("Not Found");
        errors.add(error);
        return errors;
    }
}