package com.covestro.productsservice.integrationtest;

import com.covestro.products.api.model.CategoryEnum;
import com.covestro.products.api.model.ProductReq;
import com.covestro.productsservice.util.JwtUtilTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static java.lang.Integer.MAX_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"local", "test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductsIntegrationTest {
    public static final String HTTP_LOCALHOST = "http://localhost:";
    public static final String CONTEXT_ROOT = "/api";
    public static final String GET_PRODUCTS_URI = "/products";
    @LocalServerPort
    public int port;

    private static String productId = "052de1f9-103a-4a65-a55d-7f39f8932de0";

    WireMockServer wireMockServer;
    public static final int httpPort = 8083;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(options()
                .disableRequestJournal()
                .port(httpPort)
                .jettyAcceptors(2)
                .jettyAcceptQueueSize(NumberUtils.toInt("100", MAX_VALUE)));
        wireMockServer.start();
        setupStub();
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    public void setupStub() {
        wireMockServer.stubFor(get(urlEqualTo("/api/products?page=0&pageSize=5&sort=category&name=category&category=PCS"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("json-response/products.json")));

        wireMockServer.stubFor(get(urlEqualTo("/api/products/052de1f9-103a-4a65-a55d-7f39f8932de0"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("json-response/product.json")));
        wireMockServer.stubFor(put(urlEqualTo("/api/products/052de1f9-103a-4a65-a55d-7f39f8932de0"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withFixedDelay(2500))
        );
        wireMockServer.stubFor(post(urlEqualTo("/api/products"))
                .willReturn(aResponse().
                        withStatus(200).
                        withHeader("Content-Type", "application/json")
                        .withFixedDelay(2500))
        );
    }

    @Test
    void testCreateProduct() {
        ProductReq request = getProductReq();
        given()
                .header("Authorization", JwtUtilTest.createToken())
                .request()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .log().all(true)
                .post(HTTP_LOCALHOST + httpPort + CONTEXT_ROOT + GET_PRODUCTS_URI)
                .andReturn()
                .then().log().all(true)
                .statusCode(200);
        ;
    }

    @Test
    void testGetProductById() {
        given()
                .header("Authorization", JwtUtilTest.createToken())
                .when()
                .log().all(true)
                .get(HTTP_LOCALHOST + httpPort + CONTEXT_ROOT + GET_PRODUCTS_URI + "/{product-id}",
                        productId)
                .andReturn()
                .then().log().all(true)
                .statusCode(200);
    }

    @Test
    void testUpdateProductById() {
        ProductReq request = getProductReq();
        given()
                .header("Authorization", JwtUtilTest.createToken())
                .request()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .log().all(true)
                .put(HTTP_LOCALHOST + httpPort + CONTEXT_ROOT + GET_PRODUCTS_URI + "/{product-id}",
                        productId)
                .andReturn()
                .then().log().all(true)
                .statusCode(200);
    }

    @Test
    void testGetProductsList() {
        given()
                .header("Authorization", JwtUtilTest.createToken())
                .queryParam("page", 0)
                .queryParam("pageSize", 5)
                .queryParam("sort", "category")
                .queryParam("name", "category")
                .queryParam("category", "PCS")
                .when()
                .log().all(true)
                .get(HTTP_LOCALHOST + httpPort + CONTEXT_ROOT + GET_PRODUCTS_URI)
                .andReturn()
                .then().log().all(true)
                .statusCode(200);
    }

    private ProductReq getProductReq() {
        ProductReq product = new ProductReq();
        product.setName("name");
        product.setCategory(CategoryEnum.PUR);
        product.setCurrency("EUR");
        product.setCurrentPrice(BigDecimal.valueOf(10.23));
        return product;
    }

}
