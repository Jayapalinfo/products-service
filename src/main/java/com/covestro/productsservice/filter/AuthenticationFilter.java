package com.covestro.productsservice.filter;

import com.auth0.jwt.interfaces.Claim;
import com.covestro.productsservice.config.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHENTICATION_HEADER_VALUE_PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;

    @Autowired
    public AuthenticationFilter(JwtTokenService jwtTokenService) {
        super();
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> tokenValue = getTokenValue(request);
        tokenValue.ifPresent(t -> {
            Map<String, Claim> claims = jwtTokenService.getClaimsFromToken(t);
            String user = claims.get("sub").asString();
            String role = claims.get("role").asString();
            List<SimpleGrantedAuthority> authority = Arrays.asList(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authority);
            logger.info("authenticated user role ROLE_ADMIN setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenValue(HttpServletRequest request) {
        return Optional.ofNullable(getHeaderValueByName(jwtTokenService.getHeaderName(), request).orElse(null));

    }

    private Optional<String> getHeaderValueByName(String authenticationHeaderName, HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(authenticationHeaderName))
                .map(h -> h.startsWith(AUTHENTICATION_HEADER_VALUE_PREFIX) ? h.substring(AUTHENTICATION_HEADER_VALUE_PREFIX.length()) : h);
    }

}
