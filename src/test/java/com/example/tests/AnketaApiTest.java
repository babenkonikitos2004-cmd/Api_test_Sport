package com.example.tests;


import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static com.example.tests.AdmissionApiTest.admissionId;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnketaApiTest extends BaseTest{
    protected static String cardId = "9bc4144a-4f60-4623-931c-817aa1579260";
    protected static String anketaId;

    @Test
    @Order(1)
    @DisplayName("PUT /Anketa Создание анкеты → статус 200 ")
    void createAnketa() {
        anketaId = given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body("""
                        {
                    "anketa":{
                    "createDt":"2026-05-05T11:11:17.501Z",
                    "createPosition":{
                        "person":{}
                        },
                    "lastDt":"2026-05-05T11:11:17.501Z",
                    "lastPosition":{
                        "person":{}
                        },
                    "questionnaireId":"d99e072d-340f-4d2b-809a-ce946cbe41ab"},
                    "answers":[{
                    "answer":"false",
                    "extraAnswer":"t",
                    "questionId":"b553d746-841b-419b-bfed-b7ab91b51427"},
                    {"answer":"true",
                    "extraAnswer":"tt",
                    "questionId":"c4d0df0a-1433-4eb2-b9b8-e3e9e749d0ea"},
                    {"answer":"true",
                    "questionId":"272ec4b4-0962-4359-a761-11435ed11bd0"},
                    {"answer":"true",
                    "questionId":"392b6a25-25e4-448f-82ad-f53ce40cb6c7"},
                    {"answer":"true",
                    "extraAnswer":"rwerew",
                    "questionId":"0b87b492-b227-4d32-a249-91abfcf827fa"},
                    {"answer":null,
                    "extraAnswer":"плохим",
                    "questionId":"0b8b5a73-7b16-46fb-9e8c-499c5d03ad1e"}
                    ]}
                """)
                .queryParam("cardId", cardId)
                .when()
                    .put("/Anketa")
                .then()
                .log().all()
                .statusCode(200)
                .body("anketa.id", notNullValue())
                .extract().path("anketa.id");
        System.out.print(anketaId);
    }
    @Test
    @Order(2)
    @DisplayName("GET Получение информации о анкете")
    void getAnketa() {
        given()
                .spec(baseSpec)
                .queryParam("anketaId", anketaId)
                .when()
                .get("/Anketa")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @Order(3)
    @DisplayName("POST Список допусков по карте")
    void post_anketa_cardId(){
        given()
                .spec(baseSpec)
                .queryParam("cardId", cardId)
                .when()
                .post("/Anketa")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("DELETE Удаление анкеты")
    void del_anketa(){
        given()
                .spec(baseSpec)
                .queryParam("anketaId", anketaId)
                .when()
                .delete("/Anketa")
                .then()
                .log().all()
                .statusCode(200);
    }
}
