package com.covestro.productsservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    public static final String AUTHERNTICATION_HEADER_FORMAT = "Bearer %s";
    public static final String ROLE_EXECUTOR = "ROLE_ADMIN";

    @Value("${covestro.products.jwt.signingKey:signingKey}")
    private String signingKey;
    @Value("${covestro.products.jwt.expirationMillis:10000}")
    private long expirationMillis;
    @Value("${covestro.requester}")
    private String requester;
    @Value("${covestro.products.jwt.headerName}")
    private String headerName;

    @Bean
    public JwtTokenService jwtTokenService() {
        return new JwtTokenService(signingKey, headerName);
    }

    private String createToken() {
        return JWT.create()
                .withClaim("sub", requester)
                .withClaim("role", ROLE_EXECUTOR)
                .withExpiresAt(new Date(expirationMillis + new Date().getTime()))
                .sign(Algorithm.HMAC256(signingKey.getBytes()));
    }

    public String getAuthenticationHeaderValue() {
        return String.format(AUTHERNTICATION_HEADER_FORMAT, createToken());
    }
}