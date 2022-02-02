package com.covestro.productsservice.config;

import com.auth0.jwt.interfaces.Claim;
import com.covestro.productsservice.util.JwtUtilTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JwtTokenServiceTest {

    private final JwtTokenService jwtTokenService = new JwtTokenService("LOCAL_JWT_TOKEN_SIGNING_SECRET", "AuthorizationAlt");

    @Test
    void testGetClaimsFromToken() {
        Map<String, Claim> data = jwtTokenService.getClaimsFromToken(JwtUtilTest.createToken());
        assertNotNull(data);
    }

    @Test
    void testGetClaimsFromToken_Invalid() {
        Map<String, Claim> data = jwtTokenService.getClaimsFromToken("Invalid");
        assertNull(data);
    }

    @Test
    void testGetClaimsFromToken_expired() throws InterruptedException {
        String token = JwtUtilTest.createToken();
        TimeUnit.SECONDS.sleep(5);
        Map<String, Claim> data = jwtTokenService.getClaimsFromToken(token);
        assertNotNull(data);
    }

    @Test
    void testGetSignedToken() {
        Map<String, String> claims = new HashMap<>();
        claims.put("sub", "Test");
        claims.put("role", "ADMIN");
        String token = jwtTokenService.getSignedToken(claims);
        assertNotNull(token);
    }
}
