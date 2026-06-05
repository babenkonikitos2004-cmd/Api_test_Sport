package com.example.tests;



import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class AnthropometryApiTest extends BaseTest {
    static String cardId = "9bc4144a-4f60-4623-931c-817aa1579260";
    static String Id;

    @Test
    @Order(1)
    @DisplayName("PUT /Anthropometry - создание антропометрии → статус 200")
    void putAnthropometry() {
        Id = given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body("""
                  {"anthropometry":{"sportCardId":"9bc4144a-4f60-4623-931c-817aa1579260"},"anthropometryValues":[{"anthropometryParamId":14,"value":null},{"anthropometryParamId":15,"value":"1323123"},{"anthropometryParamId":16,"value":null},{"anthropometryParamId":17,"value":null},{"anthropometryParamId":18,"value":null},{"anthropometryParamId":19,"value":null},{"anthropometryParamId":20,"value":null},{"anthropometryParamId":21,"value":null},{"anthropometryParamId":22,"value":null},{"anthropometryParamId":23,"value":null},{"anthropometryParamId":1,"value":null},{"anthropometryParamId":2,"value":"2026-05-05T14:49:25"},{"anthropometryParamId":3,"value":"12"},{"anthropometryParamId":4,"value":"123"},{"anthropometryParamId":5,"value":null},{"anthropometryParamId":6,"value":"3213"},{"anthropometryParamId":7,"value":null},{"anthropometryParamId":8,"value":null},{"anthropometryParamId":9,"value":null},{"anthropometryParamId":10,"value":null},{"anthropometryParamId":11,"value":null},{"anthropometryParamId":12,"value":"3123123"},
                  {"anthropometryParamId":13,"value":null}
                                  ]
                  }
                """)
                .when()
                .put("/Anthropometry")
                .then()
                .log().all()
                .statusCode(200)
                .body("anthropometry.id", notNullValue())
                .extract().path("anthropometry.id");
        System.out.print(Id);
    }
    @Test
    @Order(2)
    @DisplayName("GET Получение антропометрии")
    void getAnthropometry() {
        given()
                .spec(baseSpec)
                .queryParam("id", Id)
                .when()
                .get("/Anthropometry")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @Order(3)
    @DisplayName("POST Список антропометрии по карте")
    void post_Anthropometry_cardId(){
        given()
                .spec(baseSpec)
                .queryParam("cardId", cardId)
                .when()
                .post("/Anthropometry")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("DELETE Удаление антропометрии")
    void del_Anthropometry(){
        given()
                .spec(baseSpec)
                .queryParam("id", Id)
                .when()
                .delete("/Anthropometry")
                .then()
                .log().all()
                .statusCode(200);
    }
}
