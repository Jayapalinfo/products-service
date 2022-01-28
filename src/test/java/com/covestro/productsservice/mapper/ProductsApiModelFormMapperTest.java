package com.covestro.productsservice.mapper;

import com.covestro.products.api.model.CategoryEnum;
import com.covestro.products.api.model.FilteredProduct;
import com.covestro.products.api.model.Product;
import com.covestro.products.api.model.ProductReq;
import com.covestro.productsservice.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductsApiModelFormMapperTest {

    private final ProductsApiModelFormMapper productsApiModelFormMapper = new ProductsApiModelFormMapperImpl();

    private final String name = "Desmodur";

    @Test
    void testToProductsListResponse() {
        List<Product> products = productsApiModelFormMapper.toProductsListResponse(List.of(getProductEntity()));
        assertNotNull(products);
    }

    @Test
    void testToProductResponse() {
        Product product = productsApiModelFormMapper.toProductResponse(getProductEntity());
        assertNotNull(product);
    }

    @Test
    void testToProduct() {
        com.covestro.productsservice.domain.Product product = productsApiModelFormMapper.toProduct(getProductReq());
        assertNotNull(product);
    }

    @Test
    void tesUpdateProduct() {
        productsApiModelFormMapper.updateProduct(getProductReq(), getProductEntity());
    }

    @Test
    void testToFilteredProduct() {
        Page<com.covestro.productsservice.domain.Product> productPage = new PageImpl(List.of(getProductEntity()));
        FilteredProduct filteredProduct = productsApiModelFormMapper.toFilteredProduct(productPage);
        assertNotNull(filteredProduct);
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
}
