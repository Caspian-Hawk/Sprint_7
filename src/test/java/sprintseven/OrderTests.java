package sprintseven;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sprintseven.steps.OrderSteps;

import static org.hamcrest.Matchers.notNullValue;

public class OrderTests {

    private OrderSteps orderSteps = new OrderSteps();
    private String firstName;
    private String lastName;
    private String address;

    @Test
    @DisplayName("Check body response")
    public void checkBodyResponse() { // тело ответа содержит track

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstName = RandomStringUtils.randomAlphabetic(10);
        lastName = RandomStringUtils.randomAlphabetic(10);
        address = RandomStringUtils.randomAlphabetic(10);

        orderSteps
                .createOrderWithColorBlack(firstName, lastName, address)
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    @DisplayName("Check list orders in body response")
    public void checkListOrdersInBodyResponse() { // в тело ответа возвращается список заказов

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstName = RandomStringUtils.randomAlphabetic(10);
        lastName = RandomStringUtils.randomAlphabetic(10);
        address = RandomStringUtils.randomAlphabetic(10);

        // создаем заказ
        ValidatableResponse firstOrder = orderSteps
                .createOrderWithColorBlack(firstName, lastName, address)
                .statusCode(201)
                .body("track", notNullValue());

        // создаем второй заказ
        ValidatableResponse secondOrder = orderSteps
                .createOrderWithColorBlack(firstName, lastName, address)
                .statusCode(201)
                .body("track", notNullValue());

        // получаем список заказов
        ValidatableResponse listOrders = orderSteps
                .getOrders()
                .statusCode(200)
                .body("orders", notNullValue());
    }

    public void tearDown() {
        Integer id = orderSteps.createOrderWithColorBlack(firstName, lastName, address).extract().path("id");
        orderSteps.finishOrder(id);
    }
}