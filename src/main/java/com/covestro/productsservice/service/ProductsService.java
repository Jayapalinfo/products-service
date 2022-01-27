package com.covestro.productsservice.service;

import com.covestro.products.api.model.FilteredProduct;
import com.covestro.products.api.model.ProductReq;
import com.covestro.productsservice.domain.Category;
import com.covestro.productsservice.domain.Product;
import com.covestro.productsservice.exception.ProductsRuntimeException;
import com.covestro.productsservice.mapper.ProductsApiModelFormMapper;
import com.covestro.productsservice.service.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.covestro.products.api.model.Error;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductsApiModelFormMapper productsApiModelFormMapper;

    @Autowired
    public ProductsService(ProductsRepository productsRepository, ProductsApiModelFormMapper productsApiModelFormMapper) {
        this.productsRepository = productsRepository;
        this.productsApiModelFormMapper = productsApiModelFormMapper;
    }

    public FilteredProduct getAllProducts(BigDecimal page, BigDecimal pageSize, String sort, String name, String category) {

        Product product = new Product();
        if (null != name) {
            product.setName(name);
        }
        if (null != category) {
            product.setCategory(Category.valueOf(category));
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("category", ExampleMatcher.GenericPropertyMatchers.exact());//add filters for other columns here
        Example<Product> filter = Example.of(product, matcher);
        Pageable pageable = PageRequest.of(page.intValue(), pageSize.intValue(), toSort(sort));
        return productsApiModelFormMapper.toFilteredProduct(productsRepository.findAll(filter, pageable));
    }

    public com.covestro.products.api.model.Product getProductsById(String id) {
        return productsApiModelFormMapper.toProductResponse(findById(id));
    }

    public Product createProduct(ProductReq productReq) {
        Product product = productsApiModelFormMapper.toProduct(productReq);
        product.setLastUpdate(LocalDateTime.now());
        return productsRepository.save(product);
    }

    public Product findById(String id) {
        return productsRepository.findById(id).orElseThrow(() -> new ProductsRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, notFound()));
    }

    public com.covestro.products.api.model.Product updateProduct(String productId, ProductReq productReq) {
        Product product = findById(productId);
        productsApiModelFormMapper.updateProduct(productReq, product);
        return productsApiModelFormMapper.toProductResponse(productsRepository.save(product));
    }

    private Sort toSort(String sort) {
        final String[] sortVals = sort.split("[.]");
        final Sort productSort;
        if (sortVals.length == 1 || sortVals[1].equals("ASC")) {
            productSort = Sort.by(sortVals[0]).ascending();
        } else {
            productSort = Sort.by(sortVals[0]).descending();
        }
        return productSort;
    }

    private List<Error> notFound() {
        List<Error> errors = new ArrayList<>();
        Error error= new Error();
        error.setCode("404");
        error.setMessage("Not Found");
        errors.add(error);
        return errors;
    }

}
