package sprintseven.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Create courier with all required fields")
    public ValidatableResponse createCourierWithAllRequiredFields(String login, String password, String firstName) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .body("{\n" +
                        "    \"login\": \"" + login + "\",\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"firstName\": \"" + firstName + "\"\n" +
                        "}")
                .when()
                .post("/api/v1/courier")
                .then();
    }

    @Step("Create courier without parameter firstName")
    public ValidatableResponse createCourierWithoutFirstName(String login, String password) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .body("{\n" +
                        "    \"login\": \"" + login + "\",\n" +
                        "    \"password\": \"" + password + "\"\n" +
                        "}\n")
                .when()
                .post("/api/v1/courier")
                .then();
    }

    @Step("Create courier without parameters password and firstName")
    public ValidatableResponse createCourierWithoutPasswordAndFirstName(String login) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .body("{\n" +
                        "    \"login\": \"" + login + "\"\n" +
                        "}\n")
                .when()
                .post("/api/v1/courier")
                .then();
    }

    @Step("Login courier")
    public ValidatableResponse loginCourier(String login, String password) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .body("{\n" +
                        "    \"login\": \"" + login + "\",\n" +
                        "    \"password\": \"" + password + "\"\n" +
                        "}\n")
                .when()
                .post("/api/v1/courier/login")
                .then();
    }

    @Step("Login courier without login")
    public ValidatableResponse loginCourierWithoutLogin(String password) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .body("{\n" +
                        "    \"password\": \"" + password + "\"\n" +
                        "}\n")
                .when()
                .post("/api/v1/courier/login")
                .then();
    }

    @Step("Login courier without parameter password")
    public ValidatableResponse loginCourierWithoutPassword(String login) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .body("{\n" +
                        "    \"login\": \"" + login + "\"\n" +
                        "}\n")
                .when()
                .post("/api/v1/courier/login")
                .then();
    }

    @Step("Accept order")
    public ValidatableResponse acceptOrder(int orderId, int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .pathParam("id", orderId) // указываем id заказа в path параметре
                .queryParam("courierId", courierId) // передаем courierId как query параметр
                .when()
                .put("/api/v1/orders/accept/{id}")
                .then();
    }

    @Step("Delete courier")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .pathParam("id", id)
                .when()
                .delete("/api/v1/courier/{id}")
                .then();
    }
}