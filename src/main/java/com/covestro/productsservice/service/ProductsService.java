package com.covestro.productsservice.service;

import com.covestro.products.api.model.FilteredProduct;
import com.covestro.productsservice.domain.Category;
import com.covestro.productsservice.domain.Product;
import com.covestro.productsservice.service.repository.ProductsRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductsService {

    private ProductsRepository productsRepository;


    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
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
        return productsRepository.findAll(filter, pageable);
    }

    public com.covestro.products.api.model.Product getProductsById(Integer id) {
        return null;
    }

    public Product createProduct(com.covestro.products.api.model.Product productReq) {
        return null;
    }

    private Product findById(Integer id) {
        return null;
    }


    public com.covestro.products.api.model.Product updateProduct(Integer productId, com.covestro.products.api.model.Product productReq) {
        return null;
    }

    public Iterable<Product> saveAll(List<Product> users) {
        return productsRepository.saveAll(users);
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

}
