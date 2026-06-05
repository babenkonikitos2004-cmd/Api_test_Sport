package com.example.tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class AuthApiTest extends BaseTest{

    @Test
    @Order(1)
    @DisplayName("POST /login — успешная авторизация, токен получен")
    void login_success(){
        given()
                .spec(baseAdminSpec)
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
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("POST /Auth/login — не правильный пасс не 200")
    void login_wrongPassword(){
        given()
                .spec(baseAdminSpec)
                .contentType(ContentType.JSON)
                .body("""
                {
                    "user": "softmaster",
                    "password": "wrongPassword"
                }
                """)
                .when()
                .post("/Auth/login")
                .then()
                .statusCode(not(200));

    }
}
