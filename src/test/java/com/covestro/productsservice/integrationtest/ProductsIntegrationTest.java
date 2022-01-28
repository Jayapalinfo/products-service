package com.covestro.productsservice.integrationtest;

import com.covestro.products.api.model.CategoryEnum;
import com.covestro.products.api.model.ProductReq;
import com.covestro.productsservice.util.JwtUtilTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

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

    private static String productId;

    @Test
    @Order(1)
    void testCreateProduct() {
        ProductReq request = getProductReq();
        productId = given()
                .header("AuthorizationAlt", JwtUtilTest.createToken())
                .request()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .log().all(true)
                .post(HTTP_LOCALHOST + port + CONTEXT_ROOT + GET_PRODUCTS_URI)
                .andReturn()
                .then().log().all(true)
                .statusCode(200)
                .extract()
                .path("id");;
    }

    @Test
    @Order(2)
    void testGetProductById() {
        given()
                .header("AuthorizationAlt", JwtUtilTest.createToken())
                .when()
                .log().all(true)
                .get(HTTP_LOCALHOST + port + CONTEXT_ROOT + GET_PRODUCTS_URI + "/{product-id}",
                        productId)
                .andReturn()
                .then().log().all(true)
                .statusCode(200);
    }

    @Test
    @Order(3)
    void testUpdateProductById() {
        ProductReq request = getProductReq();
        given()
                .header("AuthorizationAlt", JwtUtilTest.createToken())
                .request()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .log().all(true)
                .put(HTTP_LOCALHOST + port + CONTEXT_ROOT + GET_PRODUCTS_URI + "/{product-id}",
                        productId)
                .andReturn()
                .then().log().all(true)
                .statusCode(200);
    }

    @Test
    @Order(4)
    void testGetProductsList() {
        given()
                .header("AuthorizationAlt", JwtUtilTest.createToken())
                .queryParam("page", 0)
                .queryParam("pageSize", 5)
                .queryParam("sort", "category")
                .queryParam("name", "category")
                .queryParam("category", "PCS")
                .when()
                .log().all(true)
                .get(HTTP_LOCALHOST + port + CONTEXT_ROOT + GET_PRODUCTS_URI)
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
