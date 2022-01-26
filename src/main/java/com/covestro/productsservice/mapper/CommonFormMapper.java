package com.covestro.productsservice.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CommonFormMapper {

    default LocalDate toLocalDate(String source) {
        return (LocalDate) Optional.ofNullable(source).map(LocalDate::parse).orElse((LocalDate) null);
    }
}
