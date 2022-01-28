package com.covestro.productsservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtProviderTest {
    private final JwtProvider jwtProvider = new JwtProvider();
    private final String signingKey = "LOCAL_JWT_TOKEN_SIGNING_SECRET";

    @Test
    void testGetAuthenticationHeaderValue() {
        ReflectionTestUtils.setField(jwtProvider, "signingKey", signingKey);
        String token = jwtProvider.getAuthenticationHeaderValue();
        assertNotNull(token);
    }
}
