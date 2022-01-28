package com.covestro.productsservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.covestro.productsservice.config.JwtTokenService;
import com.covestro.productsservice.util.JwtUtilTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AuthenticationFilterTest {

    private AuthenticationFilter authenticationTokenFilter;
    private JwtTokenService jwtTokenService;

    private final String headerName = "AuthorizationAlt";
    private final String signingKey = "LOCAL_JWT_TOKEN_SIGNING_SECRET";
    private final MockHttpServletResponse mockServletHttpResponse = new MockHttpServletResponse();
    private final MockFilterChain mockFilterChain = new MockFilterChain();
    private MockHttpServletRequest mockServletHttpRequest;

    @BeforeEach
    public void setUp() {
        mockServletHttpRequest = mock(MockHttpServletRequest.class);
        jwtTokenService = mock(JwtTokenService.class);
        authenticationTokenFilter = new AuthenticationFilter(jwtTokenService);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testTokenFilterInternalGivenAuthenticationInContext() throws Exception {
        String token = JwtUtilTest.createToken();
        Map<String, Claim> claimsMap = JWT.require(Algorithm.HMAC256(signingKey.getBytes()))
                .build()
                .verify(token).getClaims();
        Mockito.when(this.mockServletHttpRequest.getHeader(Mockito.anyString())).thenReturn(token);
        Mockito.when(this.jwtTokenService.getHeaderName()).thenReturn(headerName);
        Mockito.when(this.jwtTokenService.getClaimsFromToken(Mockito.anyString())).thenReturn(claimsMap);
        authenticationTokenFilter.doFilterInternal(mockServletHttpRequest, mockServletHttpResponse, mockFilterChain);
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo("Test");
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).size().isEqualTo(1);
    }

}
