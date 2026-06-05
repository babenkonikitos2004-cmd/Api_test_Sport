package com.example.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgentApiTest extends BaseTest{
    protected static String agentId;
    protected static String patientId ="ee657793-89fa-455f-8c7c-e21aed469352" ;

    @Test
    @Order(1)
    @DisplayName("Put добавление представителя")
    public void createAgentTest(){
        agentId = given()
                .spec(basePatientSpec)
                .contentType(ContentType.JSON)
                .body(""" 
                              { 
                            "agentStatusId": "e890f4d8-5313-4154-bd28-89717fdba660",
                            "patientId": "%s",
                            "surname": "string",
                            "name": "string",
                            "patron": "string",
                            "birthDt": "2026-06-05"
                            } 
               """.formatted( patientId))
                .when()
                .put("/agent")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.println(agentId);
    }

    @Test
    @Order(2)
    @DisplayName("Get Информация о представителе")
    public void getAgentTest(){
        given()
        .spec(basePatientSpec)
                .queryParam("id", agentId)
                .log().all()
                .when()
                .get("/agent")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(3)
    @DisplayName("Post Список представителей ")
    void postAgentTest(){
        given()
        .spec(basePatientSpec)
                .contentType(ContentType.JSON)
                .body(""" 
                        {
                                        "patientId": "%s",
                                        "page": {
                                          "pageNum": 0,
                                          "pageSize": 100
                                        }
                                      }
                  """.formatted(patientId))
                .when()
                .post("/agent")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    @DisplayName("Delete удаление представителя")
    void deleteAgentTest(){
        given()
        .spec(basePatientSpec)
                .queryParam("id", agentId)
                .when()
                .delete("/agent")
                .then()
                .log().all()
                .statusCode(200);
    }
}
