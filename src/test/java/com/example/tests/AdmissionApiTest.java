package com.example.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdmissionApiTest extends BaseTest{

    static String admissionId;
    static String cardId = "9bc4144a-4f60-4623-931c-817aa1579260";
    DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String begDt            = LocalDateTime.now().plusDays(5).format(dt);
    String endDt            = LocalDateTime.now().plusDays(35).format(dt);
    String restrictionBeg   = LocalDateTime.now().plusDays(10).format(dt);
    String restrictionEnd   = LocalDateTime.now().plusDays(35).format(dt);
    String createDt         = LocalDate.now().toString();
    String CreatBody = """
            {
              "admission": {
                "begDt": "%s",
                "endDt": "%s",
                "event": "test",
                "stage": "3",
                "restriction": true,
                "restrictionDesc": "test",
                "restrictionBegDt": "%s",
                "restrictionEndDt": "%s",
                "num": null,
                "sportCardId": "9bc4144a-4f60-4623-931c-817aa1579260",
                "misId": null,
                "admissionKindId": 2,
                "isLast": null,
                "createPositionId": null,
                "createDt": "%s",
                "ordId": null
              },
              "sportTypeList": [
                {
                  "sportType": { "sportTypeId": 47 },
                  "sportDisciplineList": [ { "sportDisciplineId": 874 } ]
                }
              ]
            }
            """.formatted(begDt, endDt, restrictionBeg, restrictionEnd, createDt);
    @Test
    @Order(1)
    @DisplayName("PUT /Admission/Payload — создание допуска с ограничением → статус 200")
    void create_admission_payload(){
        admissionId = given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body(CreatBody)
                .log().all()
                .when()
                .put("/Admission/Payload")
                .then()
                .log().all()
                .statusCode(200)
                .body("admission.id", notNullValue())
                .body("admission.num", notNullValue())
                .extract().path("admission.id");
        System.out.println("Допуск:" + admissionId);
    }
    @Test
    @Order(2)
    @DisplayName("PUT /Admission/Payload — создание допуска с пересечение → статус 101")
    void create_admission_payloadDuble(){
        given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body(CreatBody)
                .when()
                .put("/Admission/Payload")
                .then()
                .log().all()
                .statusCode(500)
                .body("code", equalTo("101"))
                .body("message", equalTo("Пересечение сроков допуска для одинакового вида спорта недопустимо"));
    }
    @Test
    @DisplayName("Получение информации о допуске")
    void get_admission_payload(){
        given()
                .spec(baseSpec)
                .queryParam("admissionId", admissionId)
                .when()
                .get("/Admission/Payload")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("Список допусков по карте")
    void post_admission_cardId(){
        given()
                .spec(baseSpec)
                .pathParam("cardId", cardId)
                    .when()
                .post("/Admission/{cardId}")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Удаление допуска")
    void del_admission_payload(){
        given()
                .spec(baseSpec)
                .queryParam("admissionId", admissionId)
                .when()
                .delete("/Admission")
                .then()
                .log().all()
                .statusCode(200);
    }

}
