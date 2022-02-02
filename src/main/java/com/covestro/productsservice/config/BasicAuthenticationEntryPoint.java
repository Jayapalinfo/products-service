package com.covestro.productsservice.config;

import com.covestro.products.api.model.ApiErrorResponse;
import com.covestro.products.api.model.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class BasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) {
        log.error("Unauthorized error: {}", authException.getMessage());
        try (OutputStream out = response.getOutputStream()) {
            Error error = new Error();
            error.setCode(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
            error.setMessage("Unauthorized!!!");
            ApiErrorResponse apiResponse = new ApiErrorResponse();
            apiResponse.addErrorsItem(error);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
            mapper.writeValue(out, apiResponse);
            out.flush();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
