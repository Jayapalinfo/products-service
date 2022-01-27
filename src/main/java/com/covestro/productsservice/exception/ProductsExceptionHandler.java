package com.covestro.productsservice.exception;

import com.covestro.products.api.model.ApiErrorResponse;
import com.covestro.products.api.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ProductsExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus
    @ExceptionHandler(ProductsRuntimeException.class)
    protected ResponseEntity<ApiErrorResponse> handleProductsRuntimeException(ProductsRuntimeException ex) {
        ApiErrorResponse apiErrorEnvelope = new ApiErrorResponse();
        List<Error> errors = ex.getErrors();
        log.error("Exception Caught: ", ex);
        apiErrorEnvelope.setErrors(errors);
        return new ResponseEntity<>(apiErrorEnvelope, ex.getHttpStatusCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Exception Caught AccessDenied:", ex);
        ApiErrorResponse response = new ApiErrorResponse();
        Error error = new Error();
        error.setCode("403");
        error.setMessage("Access is denied!!!");
        response.addErrorsItem(error);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

    }
}
