package de.hs.swa;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
class ExampleResourceTest {
    private static final String BASE_PATH = "/mocktails";
    @Test
    @Order(1)
    void testAddMocktail() {
        given()
                .contentType("application/json")
                .body("""
                {
                    "name": "Mojito",
                    "ingredients": [
                        { "name": "Minze", "menge": "10 Blätter" },
                        { "name": "Limette", "menge": "1 Stück" },
                        { "name": "Soda", "menge": "200 ml" }
                    ]
                }
            """)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Order(2)
    void testGetAllMocktails() {
        given()
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("$.size()", greaterThan(0))
                .body("[0].name", is("Mojito"));
    }

    @Test
    @Order(3)
    void testGetMocktailById() {
        String mocktailId = given()
                .contentType("application/json")
                .body("""
                {
                    "name": "Caipirinha",
                    "ingredients": [
                        { "name": "Limette", "menge": "1 Stück" },
                        { "name": "Zucker", "menge": "2 Teelöffel" },
                        { "name": "Cachaça", "menge": "100 ml" }
                    ]
                }
            """)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .get(BASE_PATH + "/" + mocktailId)
                .then()
                .statusCode(200)
                .body("name", is("Caipirinha"))
                .body("ingredients.size()", is(3));
    }

    @Test
    @Order(4)
    void testEditMocktail() {
        String mocktailId = given()
                .contentType("application/json")
                .body("""
                {
                    "name": "Test Mocktail",
                    "ingredients": [
                        { "name": "Zutat 1", "menge": "50 ml" }
                    ]
                }
            """)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType("application/json")
                .body("""
                {
                    "name": "Updated Mocktail",
                    "ingredients": [
                        { "name": "Zutat 1", "menge": "100 ml" },
                        { "name": "Zutat 2", "menge": "1 Stück" }
                    ]
                }
            """)
                .when()
                .patch(BASE_PATH + "/" + mocktailId)
                .then()
                .statusCode(200)
                .body(containsString("Mocktail edited successfully"));

        given()
                .when()
                .get(BASE_PATH + "/" + mocktailId)
                .then()
                .statusCode(200)
                .body("name", is("Updated Mocktail"))
                .body("ingredients.size()", is(2));
    }

    @Test
    @Order(5)
    void testDeleteMocktail() {
        String mocktailId = given()
                .contentType("application/json")
                .body("""
                {
                    "name": "Mocktail to Delete",
                    "ingredients": [
                        { "name": "Zutat 1", "menge": "30 ml" }
                    ]
                }
            """)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .delete(BASE_PATH + "/" + mocktailId)
                .then()
                .statusCode(200)
                .body(containsString("Mocktail deleted successfully"));

        given()
                .when()
                .get(BASE_PATH + "/" + mocktailId)
                .then()
                .statusCode(404);
    }
}