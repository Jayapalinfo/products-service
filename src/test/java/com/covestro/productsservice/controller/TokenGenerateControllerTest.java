package com.covestro.productsservice.controller;

import com.covestro.products.api.model.FilteredProduct;
import com.covestro.productsservice.config.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class TokenGenerateControllerTest {

    private JwtProvider jwtProvider;
    private TokenGenerateController authenticationController;
    private final String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBRE1JTiIsInJvbGUiOiJST0xFX0FETUlOIiwiZXhwIjoxNjQzMjM2ODM1fQ.b-Ne7cZHBrE_WigZBWWT57Qn3YgsPU6kMmTF2DcK_WM";

    @BeforeEach
    void setup() {
        jwtProvider = mock(JwtProvider.class);
        authenticationController = new TokenGenerateController(jwtProvider);
    }

    @Test
    void testGetJwtToken_success() {
        Mockito
                .when(this.jwtProvider.getAuthenticationHeaderValue())
                .thenReturn(token);
        ResponseEntity<FilteredProduct> result = authenticationController.getGeneratedToken();
        assertNotNull(ResponseEntity.ok(result));
    }
}
