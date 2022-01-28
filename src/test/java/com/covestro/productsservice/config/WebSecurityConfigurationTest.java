package com.covestro.productsservice.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebSecurityConfigurationTest {

    private JwtTokenService jwtTokenService;
    private final WebSecurityConfiguration webSecurityConfiguration = new WebSecurityConfiguration(jwtTokenService);

    @Test
    void testAuthenticatedEntryPoint() {
        assertNotNull(webSecurityConfiguration.authenticationEntryPoint());
    }
}
