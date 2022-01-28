package com.covestro.productsservice.exception;

import com.covestro.products.api.model.ApiErrorResponse;
import com.covestro.products.api.model.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

public class ProductsExceptionHandlerTest {
    private final ProductsExceptionHandler productsExceptionHandler = new ProductsExceptionHandler();

    @Test
    void testHandleProductsRuntimeException() {
        ResponseEntity<ApiErrorResponse> response = productsExceptionHandler.handleProductsRuntimeException(getProductsRuntimeException());
        Assertions.assertNotNull(response);
    }

    @Test
    void testHandleAccessDeniedException() {
        ResponseEntity<ApiErrorResponse> response = productsExceptionHandler.handleAccessDeniedException(new AccessDeniedException("Access Denied"));
        Assertions.assertNotNull(response);
    }

    private ProductsRuntimeException getProductsRuntimeException() {
        List<Error> errors = new ArrayList<>();
        Error error = new Error();
        error.setMessage("Internal Server Error");
        error.setCode("500");
        errors.add(error);
        return new ProductsRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, errors);
    }
}
