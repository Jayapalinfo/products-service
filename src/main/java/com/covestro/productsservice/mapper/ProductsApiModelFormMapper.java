package com.covestro.productsservice.mapper;

import com.covestro.products.api.model.FilteredProduct;
import com.covestro.productsservice.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductsApiModelFormMapper {
    @Mappings({
            @Mapping(source = "currentPrice", target = "price"),
    })
    List<com.covestro.products.api.model.Product> toProductsListResponse(List<Product> product);

    @Mappings({
            @Mapping(source = "currentPrice", target = "price"),
    })

    default FilteredProduct toFilteredProduct(Page<Product> page) {
        return new FilteredProduct()
                .products(toProductsListResponse(page.getContent()))
                .number((long) page.getNumber())
                .size((long) (page.getSize()))
                .totalElements(page.getTotalElements())
                .totalPages((long) (page.getTotalPages()));
    }

}
