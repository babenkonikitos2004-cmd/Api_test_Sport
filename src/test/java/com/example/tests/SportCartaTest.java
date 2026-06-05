package com.example.tests;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SportCartaTest extends BaseTest{
    static String cardId;
    static String sportsId;
    static String nationalTeamId;
    static  String сompetitionId;

    @Test
    @Order(1)
    @DisplayName("PUT /Card - создание карты спортсмена → статус 200")
    void putCard() {
        cardId = given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body("""
                  {"patientId":"ee657793-89fa-455f-8c7c-e21aed469352",  "id": "847c3ff2-fd68-4244-ac12-ff1e1e45f527" }
                """)
                .when()
                .put("/Card")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.print(cardId);
    }
    @Test
    @Order(2)
    @DisplayName("PUT /Sports - создание Вида спорта → статус 200")
    void putSports() {
        sportsId = given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body(String.format("""
                  {"sport":{"sportCardId":"%s","coachId":"1","fioDoctor":null,"id":null,"misId":null,"sportActivitiesId":"2","sportCategoryId":"2","sportLevelId":"2","sportOrgId":"dd185136-cc9e-4757-afc7-f0c358a3cca9","sportTypeId":"11","trainingDuration":3,"trainingInWeek":"4","yearBegin":"2000"},"disciplines":[{"sportDisciplineId":"167","sportsId":null}]}
                """,cardId))
                .when()
                .put("/Card/Sports")
                .then()
                .log().all()
                .statusCode(200)
                .body("sport.id", notNullValue())
                .extract().path("sport.id");
        System.out.print(sportsId);
    }
    @Test
    @Order(3)
    @DisplayName("GET Получение вида спорта")
    void getSports() {
        given()
                .spec(baseSpec)
                .queryParam("id", sportsId)
                .when()
                .get("/Card/Sports")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @Order(4)
    @DisplayName("POST Список видов спорта по карте")
    void post_Sports_cardId(){
        given()
                .spec(baseSpec)
                .queryParam("cardId", cardId)
                .when()
                .post("/Card/Sports")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("DELETE Удаление вида спорта")
    void del_Sports(){
        given()
                .spec(baseSpec)
                .queryParam("id", sportsId)
                .when()
                .delete("/Card/Sports")
                .then()
                .log().all()
                .statusCode(200);
    }


    @Test
    @Order(5)
    @DisplayName("PUT /Card/NationalTeam - создание участия в сборной → статус 200")
    void putNationalTeam() {
        nationalTeamId = given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body(String.format("""
                  {"sportCardId":"%s","sportsId":"%s","id":null,"begDt":"2026-05-07T00:00:00","endDt":"2026-05-22T00:00:00","sportCastId":"1"}
                """,cardId,sportsId))
                .when()
                .put("/Card/NationalTeam")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.print(nationalTeamId);
    }
    @Test
    @Order(6)
    @DisplayName("GET Получение участия в сборной")
    void getNationalTeam() {
        given()
                .spec(baseSpec)
                .queryParam("id", nationalTeamId)
                .when()
                .get("/Card/NationalTeam")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @Order(7)
    @DisplayName("POST Список участия в сборной по карте")
    void post_NationalTea_cardId(){
        given()
                .spec(baseSpec)
                .queryParam("cardId", cardId)
                .when()
                .post("/Card/NationalTeam")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("DELETE Удаление участия в сборной")
    void del_NationalTea(){
        given()
                .spec(baseSpec)
                .queryParam("id", nationalTeamId)
                .when()
                .delete("/Card/NationalTeam")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(8)
    @DisplayName("PUT /Card/Competition - создание участия в соревнованиях → статус 200")
    void putCompetition() {
        сompetitionId = given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body(String.format("""
                  {"sportCardId":"%s","disciplines":[],"dt":"2026-05-08T00:00:00","endDt":"2026-05-29T00:00:00","id":null,"name":"Еврокуб","sportsId":"%s"}
                """,cardId,sportsId))
                .when()
                .put("/Card/Competition")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.print(сompetitionId);
    }
    @Test
    @Order(9)
    @DisplayName("GET Получение участия в соревнованиях")
    void getCompetition() {
        given()
                .spec(baseSpec)
                .queryParam("id", сompetitionId)
                .when()
                .get("/Card/Competition")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @Order(7)
    @DisplayName("POST Список участия в соревнованиях по карте")
    void post_Competition_cardId(){
        given()
                .spec(baseSpec)
                .queryParam("cardId", cardId)
                .when()
                .post("/Card/Competition")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("DELETE Удаление участия в сборной")
    void del_Competition(){
        given()
                .spec(baseSpec)
                .queryParam("id", сompetitionId)
                .when()
                .delete("/Card/Competition")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("POST /Registry - список спортсменов → статус 200")
    void AllSpots_Registry() {
        given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body("""
                  {"admissionBegDt":{"begDt":null,"endDt":null},"admissionEndDt":{"begDt":null,"endDt":null},"admissionEnds":null,"age":null,"ageEnd":null,"birthDt":null,"coachId":null,"orgId":{"value":[],"typeFind":"in"},"page":{"pageNum":0,"pageSize":25},"sexId":null,"sportOrgId":{"value":[],"typeFind":"in"},"sportTypeId":null}
                """)
                .when()
                .post("/Registry")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("POST /Registry/Excel - выгрузка в ексель списока спортсменов → статус 200")
    void Registry_Excel() {
        given()
                .spec(baseSpec)
                .contentType(ContentType.JSON)
                .body("""
                  {"admissionBegDt":{"begDt":null,"endDt":null},"admissionEndDt":{"begDt":null,"endDt":null},"admissionEnds":null,"age":null,"ageEnd":null,"birthDt":null,"coachId":null,"orgId":{"value":[],"typeFind":"in"},"page":{"pageNum":2,"pageSize":25},"sexId":null,"sportOrgId":{"value":[],"typeFind":"in"},"sportTypeId":null}
                """)
                .when()
                .post("/Registry/Excel")
                .then()
                .log().all()
                .statusCode(200);
    }


}
