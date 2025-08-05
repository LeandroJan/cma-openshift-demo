package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemResourceTest {

    @Test
    @Order(1)
    public void testGetAllItemsEndpoint() {
        given()
                .when().get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", is(0)); // Initially empty
    }

    @Test
    @Order(2)
    public void testCreateItemEndpoint() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Test Item\"}")
                .when().post("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", is("Test Item"))
                .body("id", notNullValue());
    }

    @Test
    @Order(3)
    public void testGetItemByIdEndpoint() {
        // First create an item
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Another Test Item\"}")
                .when().post("/items")
                .then()
                .statusCode(200);

        // Then retrieve it
        given()
                .when().get("/items/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", is(1))
                .body("name", notNullValue());
    }

    @Test
    @Order(4)
    public void testDeleteItemEndpoint() {
        // First ensure we have at least one item to delete
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Item to Delete\"}")
                .when().post("/items")
                .then()
                .statusCode(200);

        // Get current count of items
        Integer initialCount = given()
                .when().get("/items")
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("size()");

        // Delete the item with ID 1
        given()
                .when().delete("/items/1")
                .then()
                .statusCode(204); // DELETE returns 204 No Content

        // Verify deletion by checking the count decreased
        given()
                .when().get("/items")
                .then()
                .statusCode(200)
                .body("size()", is(initialCount - 1));
    }
}
