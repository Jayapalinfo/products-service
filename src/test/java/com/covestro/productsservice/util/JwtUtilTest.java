package com.covestro.productsservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtUtilTest {
    public static String createToken() {
        return JWT.create()
                .withClaim("sub", "Test")
                .withClaim("role", "ROLE_ADMIN")
                .withExpiresAt(new Date(123456 + new Date().getTime()))
                .sign(Algorithm.HMAC256("LOCAL_JWT_TOKEN_SIGNING_SECRET".getBytes()));
    }
}
