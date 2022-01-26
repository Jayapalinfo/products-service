package com.covestro.productsservice.exception;

import com.covestro.products.api.model.Error;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductsRuntimeException extends RuntimeException {
    private HttpStatus httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    private List<Error> errors;

    public ProductsRuntimeException(HttpStatus httpStatus, List<Error> errors) {
        this.httpStatusCode = httpStatus;
        setErrors(errors);
    }

    private void setErrors(List<Error> errors) {
        this.errors = Collections.unmodifiableList(errors);
    }

}
