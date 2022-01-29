package com.covestro.productsservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtTokenService {

    private final String signingKey;
    private final String headerName;

    public static final long TOKEN_EXPIRY_AFTER = 300000;

    public JwtTokenService(String signingKey, String headerName) {
        this.signingKey = signingKey;
        this.headerName = headerName;
    }

    public String getHeaderName(){
        return this.headerName;
    }

    public Map<String, Claim> getClaimsFromToken(String signingKey, String token) {
        try {
            return JWT.require(Algorithm.HMAC256(signingKey.getBytes()))
                    .build()
                    .verify(token).getClaims();
        } catch (JWTVerificationException ex) {
            log.error("JWT Verification failed {}", ex.getMessage());
        }
        return null;
    }

    public Map<String, Claim> getClaimsFromToken(String token) {
        return getClaimsFromToken(signingKey, token);
    }

    public String getSignedToken(Map<String, String> claims) {
        return getSignedToken(claims, this.signingKey);
    }

    public String getSignedToken(Map<String, String> claims, String key) {
        JWTCreator.Builder builder = JWT.create();
        claims.forEach((k, v) -> builder.withClaim(k, v));

        return builder.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRY_AFTER))
                .sign(Algorithm.HMAC256(key.getBytes()));
    }

}
