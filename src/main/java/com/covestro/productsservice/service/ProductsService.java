package com.covestro.productsservice.service;

import com.covestro.products.api.model.Error;
import com.covestro.products.api.model.FilteredProduct;
import com.covestro.products.api.model.ProductReq;
import com.covestro.productsservice.domain.Product;
import com.covestro.productsservice.exception.ProductsRuntimeException;
import com.covestro.productsservice.mapper.ProductsApiModelFormMapper;
import com.covestro.productsservice.service.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
        product.setName(name);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("category", ExampleMatcher.GenericPropertyMatchers.exact()); //add filters for other columns here
        Example<Product> filter = Example.of(product, matcher);
        Pageable pageable = PageRequest.of(page.intValue(), pageSize.intValue(), toSort(sort));
        return productsApiModelFormMapper.toFilteredProduct(productsRepository.findAll(filter, pageable));
    }

    public com.covestro.products.api.model.Product getProductsById(String id) {
        return productsApiModelFormMapper.toProductResponse(productsRepository.findById(id).orElseThrow(() -> new ProductsRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, notFound())));
    }

    public com.covestro.products.api.model.Product createProduct(ProductReq productReq) {
        Product product = productsApiModelFormMapper.toProduct(productReq);
        product.setLastUpdate(LocalDateTime.now());
        return productsApiModelFormMapper.toProductResponse(productsRepository.save(product));
    }

    private Product findById(String id) {
        return productsRepository.findById(id).orElseThrow(() -> new ProductsRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, notFound()));
    }

    public com.covestro.products.api.model.Product updateProduct(String productId, ProductReq productReq) {
        Product product = findById(productId);
        productsApiModelFormMapper.updateProduct(productReq, product);
        return productsApiModelFormMapper.toProductResponse(productsRepository.save(product));
    }

    public Iterable<Product> saveAll(List<Product> users) {
        return productsRepository.saveAll(users);
    }

    private Sort toSort(String sort) {
        final String[] sortVals = sort.split("[.]");
        final Sort loroSort;
        if (sortVals.length == 1 || sortVals[1].equals("ASC")) {
            loroSort = Sort.by(sortVals[0]).ascending();
        } else {
            loroSort = Sort.by(sortVals[0]).descending();
        }
        return loroSort;
    }

    private List<Error> notFound() {
        List<Error> errors = new ArrayList<>();
        Error error = new Error();
        error.setCode("404");
        error.setMessage("Not Found");
        errors.add(error);
        return errors;
    }
}
