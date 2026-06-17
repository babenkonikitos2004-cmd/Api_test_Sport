package com.example.tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProtocolApiTest extends BaseTest {

    // ===== ID, переиспользуемые между тестами =====
    static String examinationId;   // осмотр
    static String examNazId;       // назначение внутри осмотра
    static String nazId;           // назначение (/naz)
    static String nazDictId;       // назначение из справочника
    static String nazGroupId;      // группа назначений
    static String paramId;         // параметр
    static String umoId;           // запись справочника УМО
    static String umoNazId;        // назначение из справочника УМО
    static String sportCardId = "e6b0e344-694e-408f-b700-6b0bca32ab90" ;

    /* =========================================================
       ОСМОТР  (/examination)
       ========================================================= */

    @Test
    @Order(1)
    @DisplayName("PUT /examination - добавление / редактирование осмотра → 200")
    void putExamination() {
        examinationId = given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body(String.format("""
                  {
                    "begDt": "2026-06-17",
                    "statusId" : "4bcb5ea9-773e-470b-927a-99e4abfa24b3",
                    "typeId": "2c9c7208-66b6-488e-b1b7-1755bdb030c3",
                    "sportCardId": "%s"
                  }
                """, sportCardId)) // TODO: тело осмотра
                .when()
                .put("/examination")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.println(examinationId);
    }

    @Test
    @Order(2)
    @DisplayName("GET /examination - получение осмотра → 200")
    void getExamination() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", examinationId)
                .when()
                .get("/examination")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(3)
    @DisplayName("POST /examination - список осмотров → 200")
    void getExaminationList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body(String.format("""
                  {
                  "cardId": "%s",
                    "page": {
                      "pageNum": 0,
                      "pageSize": 100
                    }
                  }
                """, sportCardId)) // TODO: тело фильтра списка осмотров
                .when()
                .post("/examination")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       НАЗНАЧЕНИЕ В ОСМОТРЕ  (/examination/naz)
       ========================================================= */

    @Test
    @Order(4)
    @DisplayName("PUT /examination/naz - добавление нового назначения в осмотр → 200")
    void putExaminationNaz() {
        examNazId = given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                            "naz": {
                              "statusId": "f9594e40-ee7f-436b-8bfe-5b5cf62d85f4",
                              "begDt": "2026-05-27",
                              "endDt": "2026-05-25",
                              "dictId": "0ba9f3d8-91df-4aad-aaee-aac96d316ecb"
                            },
                            "params": [
                              {
                                 "paramId": "f8ac7230-44cf-4301-8665-eab995e69d1e",
                                "value": "string"
                              }
                            ]
                          }
                """) // TODO: тело назначения (examinationId уже подставляется)
                .queryParam("id", examinationId)
                .when()
                .put("/examination/naz")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.println(examNazId);
    }

    @Test
    @Order(5)
    @DisplayName("POST /examination/naz - список назначений осмотра → 200")
    void getExaminationNazList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .queryParam("examinationId", examinationId)
                .when()
                .post("/examination/naz")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       ПОДПИСЬ ОСМОТРА  (/examination/sign)
       ========================================================= */

    @Test
    @Order(6)
    @DisplayName("PUT /examination/sign - подпись осмотра → 200")
    void putSignExamination() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body(String.format("""
                  {
                          "id": "%s",
                            "admitted": true
                          }
                """, examinationId)) // TODO: тело подписи
                .when()
                .put("/examination/sign")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(7)
    @DisplayName("DELETE /examination/sign - снятие подписи осмотра → 200")
    void deleteSignExamination() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", examinationId)
                .when()
                .delete("/examination/sign")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       НАЗНАЧЕНИЕ  (/naz)
       ========================================================= */

    @Test
    @Order(8)
    @DisplayName("GET /naz - получение назначения → 200")
    void getNaz() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", examNazId) // TODO: при необходимости заменить на nazId
                .when()
                .get("/naz")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("DELETE /naz - удаление назначения → 200")
    void deleteNaz() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", examNazId) // TODO: при необходимости заменить на nazId
                .when()
                .delete("/naz")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       СПРАВОЧНИК НАЗНАЧЕНИЙ  (/naz/dict)
       ========================================================= */

    @Test
    @Order(10)
    @DisplayName("PUT /naz/dict - добавление / редактирование назначения в справочник → 200")
    void putNazDict() {
        nazDictId = given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "string",
                    "groupId": "a2539f85-80b9-477f-8f60-e5f6697a54d7",
                    "requireProtocol": true,
                    "deleted": true,
                    "typeId": "1b446c37-ccde-4264-8905-323ec1564f2e",
                    "sort": 0
                  }
                """) // TODO: тело назначения справочника
                .when()
                .put("/naz/dict")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.println(nazDictId);
    }

    @Test
    @Order(11)
    @DisplayName("GET /naz/dict - получение назначения из справочника → 200")
    void getNazDict() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", nazDictId)
                .when()
                .get("/naz/dict")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(12)
    @DisplayName("POST /naz/dict - список назначений → 200")
    void getNazDictList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "page": {
                      "pageNum": 0,
                      "pageSize": 100
                    }
                  }
                """) // TODO: тело фильтра
                .when()
                .post("/naz/dict")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(13)
    @DisplayName("POST /naz/dict/param - список параметров назначения → 200")
    void getNazDictParamList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .queryParam("nazDictId", nazDictId)
                .when()
                .post("/naz/dict/param")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("DELETE /naz/dict - удаление назначения из справочника → 200")
    void deleteNazDict() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", nazDictId)
                .when()
                .delete("/naz/dict")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       ГРУППЫ НАЗНАЧЕНИЙ  (/naz/group)
       ========================================================= */

    @Test
    @Order(15)
    @DisplayName("PUT /naz/group - добавление / редактирование группы назначений → 200")
    void putNazGroup() {
        nazGroupId = given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body(String.format("""
                    {
                    "id": "%s",
                      "parentId": "068829ee-711f-480f-8ce7-dae442e4ab64",
                      "name": "string",
                      "orgStructId": "75e867ad-2e56-4db6-9bf5-7bce0193c25c"
                    }
                """,examNazId)) // TODO: тело группы
                .when()
                .put("/naz/group")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.println(nazGroupId);
    }

    @Test
    @Order(16)
    @DisplayName("GET /naz/group - получение группы назначений из справочника → 200")
    void getNazGroup() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", nazGroupId)
                .when()
                .get("/naz/group")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(17)
    @DisplayName("POST /naz/group - список групп назначений → 200")
    void getNazGroupList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "string"
                  }
                """) // TODO: тело фильтра
                .when()
                .post("/naz/group")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(18)
    @DisplayName("POST /naz/group/dict - список групп с назначениями → 200")
    void getNazGroupDictList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "string",
                    "syn": "string"
                  }
                """) // TODO: тело фильтра
                .when()
                .post("/naz/group/dict")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(19)
    @DisplayName("DELETE /naz/group - удаление группы назначений из справочника → 200")
    void deleteNazGroup() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", nazGroupId)
                .when()
                .delete("/naz/group")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       ПАРАМЕТРЫ  (/param)
       ========================================================= */

    @Test
    @Order(20)
    @DisplayName("PUT /param - добавление / редактирование параметра → 200")
    void putParam() {
        paramId = given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "string",
                    "deleted": true,
                    "dataTypeId": 1,
                    "dictSql": "string"
                  }
                """) // TODO: тело параметра
                .when()
                .put("/param")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.println(paramId);
    }

    @Test
    @Order(21)
    @DisplayName("GET /param - получение параметра → 200")
    void getParam() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", paramId)
                .when()
                .get("/param")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(22)
    @DisplayName("POST /param - справочник параметров назначения → 200")
    void getParamList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "page": {
                      "pageNum": 0,
                      "pageSize": 10
                    }
                  }
                """) // TODO: тело фильтра
                .when()
                .post("/param")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(23)
    @DisplayName("DELETE /param - удаление параметра → 200")
    void deleteParam() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", paramId)
                .when()
                .delete("/param")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       СПРАВОЧНИК УМО  (/umo)
       ========================================================= */

    @Test
    @Order(24)
    @DisplayName("PUT /umo - добавление / редактирование записи справочника УМО → 200")
    void putUmoDict() {
        umoId = given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "name": "string",
                    "sportLevelId": 1,
                    "periodicity": 2,
                    "orgStructId": "75e867ad-2e56-4db6-9bf5-7bce0193c25c"
                  }
                """) // TODO: тело записи УМО
                .when()
                .put("/umo")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.println(umoId);
    }

    @Test
    @Order(25)
    @DisplayName("GET /umo - получение записи справочника УМО → 200")
    void getUmoDict() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", umoId)
                .when()
                .get("/umo")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(26)
    @DisplayName("POST /umo - справочник УМО → 200")
    void getUmoDictList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "page": {
                      "pageNum": 0,
                      "pageSize": 10
                    }
                  }
                """) // TODO: тело фильтра
                .when()
                .post("/umo")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       НАЗНАЧЕНИЯ ИЗ СПРАВОЧНИКА УМО  (/umo/naz)
       ========================================================= */

    @Test
    @Order(27)
    @DisplayName("PUT /umo/naz - добавление / редактирование назначения в справочник УМО → 200")
    void putUmoNazDict() {
        umoNazId = given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body(String.format("""
                  {
                            "umoDictId": "%s",
                            "nazDictId": "%s",
                            "begAge": 0,
                            "endAge": 0,
                            "sexId": "17d15624-631e-45fd-baf7-b2cffc68d081",
                            "desc": "string"
                          }
                """, umoId, nazDictId)) // TODO: тело назначения УМО (umoId уже подставляется)
                .when()
                .put("/umo/naz")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
        System.out.println(umoNazId);
    }

    @Test
    @Order(28)
    @DisplayName("GET /umo/naz - получение назначения из справочника УМО → 200")
    void getUmoNazDict() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", umoNazId)
                .when()
                .get("/umo/naz")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(29)
    @DisplayName("POST /umo/naz - назначения из справочника УМО → 200")
    void getUmoNazDictList() {
        given()
                .spec(baseSpecProtocol)
                .contentType(ContentType.JSON)
                .body("""
                  {
                    "page": {
                      "pageNum": 0,
                      "pageSize": 10
                    }
                  }
                """) // TODO: тело фильтра (umoId уже подставляется)
                .queryParam("umoDictId", umoId)
                .when()
                .post("/umo/naz")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(30)
    @DisplayName("DELETE /umo/naz - удаление назначения из справочника УМО → 200")
    void deleteUmoNazDict() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", umoNazId)
                .when()
                .delete("/umo/naz")
                .then()
                .log().all()
                .statusCode(200);
    }


    /* =========================================================
       УДАЛЕНИЕ ЗАПИСИ УМО + ОСМОТРА (в самом конце цепочки)
       ========================================================= */

    @Test
    @Order(31)
    @DisplayName("DELETE /umo - удаление записи справочника УМО → 200")
    void deleteUmoDict() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", umoId)
                .when()
                .delete("/umo")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(32)
    @DisplayName("DELETE /examination - удаление осмотра → 200")
    void deleteExamination() {
        given()
                .spec(baseSpecProtocol)
                .queryParam("id", examinationId)
                .when()
                .delete("/examination")
                .then()
                .log().all()
                .statusCode(200);
    }
}