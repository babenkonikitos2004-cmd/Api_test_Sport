// BaseTest.java — общий для всех тестов
package com.example.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class BaseTest {
    protected static String token;
    protected static RequestSpecification baseSpec;
    protected static RequestSpecification baseAdminSpec;
    protected  static String profileToken;

    @BeforeAll
    static void setUp() {
        // Логин — получаем токен
        token = given()
                .baseUri("http://sportmed.oblteh:8081")
                .contentType(ContentType.JSON)
                .body("""
                {
                    "user": "softmaster",
                    "password": "sys1okan"
                }
                """)
                .when()
                .post("/Auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().path("token");
        System.out.print(token);


        profileToken = given()
                .baseUri("http://sportmed.oblteh:8081")
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/Profile")
                .then()
                .statusCode(200)
                .body("[0].profileToken", notNullValue())
                .extract().path("[0].profileToken");

        // Спека для админки (порт 8081)
        baseAdminSpec = new RequestSpecBuilder()
                .setBaseUri("http://sportmed.oblteh:8081")
                .addHeader("Authorization", "Bearer " + token)
                .setContentType(ContentType.JSON)
                .build();

        // Спека для спортмед API (порт 8083)
        baseSpec = new RequestSpecBuilder()
                .setBaseUri("http://sportmed.oblteh:8083")
                .addHeader("Authorization", "Bearer " + profileToken)
                .setContentType(ContentType.JSON)
                .build();
    }
}