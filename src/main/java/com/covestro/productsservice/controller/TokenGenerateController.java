package com.covestro.productsservice.controller;

import com.covestro.productsservice.config.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;

@Controller
@Slf4j
@RequiredArgsConstructor
@Validated
public class TokenGenerateController {

    private JwtProvider jwtProvider;

    @Autowired
    TokenGenerateController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @RequestMapping(value = "/generate-token", method = RequestMethod.GET)
    public ResponseEntity getGeneratedToken() {
        final String token = jwtProvider.createToken();
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("token", token));
    }
}
