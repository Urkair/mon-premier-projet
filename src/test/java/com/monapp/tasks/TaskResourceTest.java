package com.monapp.tasks;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class TaskResourceTest {

    // Utilitaire pour obtenir un token
    private String getToken(String username, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when().post("/auth/login")
                .then().statusCode(200)
                .extract().path("token");
    }

    @Test
    void testListerTachesSansToken() {
        given()
                .when().get("/tasks")
                .then().statusCode(401);
    }

    @Test
    void testListerTachesAvecToken() {
        String token = getToken("user", "secret");
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/tasks")
                .then().statusCode(200);
    }

    @Test
    void testCreerTacheValide() {
        String token = getToken("user", "secret");
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"titre\":\"Test tache\",\"description\":\"Description test\"}")
                .when().post("/tasks")
                .then()
                .statusCode(201)
                .body("titre", equalTo("Test tache"))
                .body("terminee", equalTo(false));
    }

    @Test
    void testCreerTacheSansTitre() {
        String token = getToken("user", "secret");
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"titre\":\"\",\"description\":\"Description test\"}")
                .when().post("/tasks")
                .then()
                .statusCode(400);
    }

    @Test
    void testSupprimerTacheUserRefuse() {
        String token = getToken("user", "secret");
        given()
                .header("Authorization", "Bearer " + token)
                .when().delete("/tasks/1")
                .then().statusCode(403);
    }

    @Test
    void testSupprimerTacheAdmin() {
        String adminToken = getToken("admin", "admin123");

        // Créer une tâche d'abord
        Integer id = given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body("{\"titre\":\"Tache a supprimer\",\"description\":\"Test\"}")
                .when().post("/tasks")
                .then().statusCode(201)
                .extract().path("id");

        // Supprimer la tâche
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when().delete("/tasks/" + id)
                .then().statusCode(204);
    }
}