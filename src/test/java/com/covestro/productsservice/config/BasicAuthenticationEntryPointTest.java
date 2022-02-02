package com.covestro.productsservice.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasicAuthenticationEntryPointTest {
    private final BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();

    @Test
    void commence() throws IOException {

        ServletOutputStream outputStream = Mockito.mock(ServletOutputStream.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        AuthenticationException exception = new AuthenticationException("") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
        Mockito
                .when(response.getOutputStream())
                .thenReturn(outputStream);
        basicAuthenticationEntryPoint.commence(request, response, exception);
    }
}
