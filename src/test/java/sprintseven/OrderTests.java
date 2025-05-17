package sprintseven;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import sprintseven.steps.OrderSteps;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTests {

    private OrderSteps orderSteps = new OrderSteps();
    private final Faker faker = new Faker();
    private String firstName;
    private String lastName;
    private String address;

    @Test
    @DisplayName("Test body response")
    @Description("Этот тест проверяет возврат в ответе номера заказа")
    public void testBodyResponse() { // тело ответа содержит track

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstName = faker.name().username();
        lastName = faker.internet().password();
        address = faker.name().name();

        orderSteps
                .createOrderWithColorBlack(firstName, lastName, address)
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());
    }

    @Test
    @DisplayName("Test list orders in body response")
    @Description("Этот тест проверяет возврат в ответе список заказов")
    public void testListOrdersInBodyResponse() { // в тело ответа возвращается список заказов

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstName = faker.name().username();
        lastName = faker.internet().password();
        address = faker.name().name();

        // создаем заказ
        ValidatableResponse firstOrder = orderSteps
                .createOrderWithColorBlack(firstName, lastName, address)
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());

        // создаем второй заказ
        ValidatableResponse secondOrder = orderSteps
                .createOrderWithColorBlack(firstName, lastName, address)
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());

        // получаем список заказов
        ValidatableResponse listOrders = orderSteps
                .getOrders()
                .statusCode(HttpStatus.SC_OK)
                .body("orders", notNullValue());
    }

    public void tearDown() {
        Integer id = orderSteps.createOrderWithColorBlack(firstName, lastName, address).extract().path("id");
        orderSteps.finishOrder(id);
    }
}