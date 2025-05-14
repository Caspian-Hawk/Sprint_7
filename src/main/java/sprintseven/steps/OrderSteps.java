package sprintseven.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Create order with multiple colors")
    public ValidatableResponse createOrderWithMultipleColors(String firstName, String lastName, String address, String[] colors) {
        String jsonColors = (colors.length > 0) ? String.format("\"%s\"", String.join("\",\"", colors)) : "";

        String requestBody = String.format("{\n" +
                "    \"firstName\": \"%s\",\n" +
                "    \"lastName\": \"%s\",\n" +
                "    \"address\": \"%s\",\n" +
                "    \"metroStation\": 4,\n" +
                "    \"phone\": \"+7 800 355 35 35\",\n" +
                "    \"rentTime\": 5,\n" +
                "    \"deliveryDate\": \"2025-05-09\",\n" +
                "    \"comment\": \"Saske, come back to Konoha\",\n" +
                "    \"color\": [%s]\n" +
                "}", firstName, lastName, address, jsonColors);

        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .body(requestBody)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    @Step("Create order with color black")
    public ValidatableResponse createOrderWithColorBlack(String firstName, String lastName, String address) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .body("{\n" +
                        "    \"firstName\": \"" + firstName + "\",\n" +
                        "    \"lastName\": \"" + lastName + "\",\n" +
                        "    \"address\": \"" + address + "\",\n" +
                        "    \"metroStation\": 4,\n" +
                        "    \"phone\": \"+7 800 355 35 35\",\n" +
                        "    \"rentTime\": 5,\n" +
                        "    \"deliveryDate\": \"2020-06-06\",\n" +
                        "    \"comment\": \"Saske, come back to Konoha\",\n" +
                        "    \"color\": [\n" +
                        "        \"BLACK\"\n" +
                        "    ]\n" +
                        "}")
                .when()
                .post("/api/v1/orders")
                .then();
    }

    @Step("Get orders")
    public ValidatableResponse getOrders() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .when()
                .get("/api/v1/orders").then();
    }

    @Step("Finish order")
    public ValidatableResponse finishOrder(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .pathParam("id", id)
                .when()
                .put("/api/v1/orders/finish/{id}")
                .then();
    }
}