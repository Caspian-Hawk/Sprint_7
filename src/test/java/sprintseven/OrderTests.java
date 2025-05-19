package sprintseven;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import sprintseven.steps.OrderSteps;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTests { // заказы

    private OrderSteps orderSteps = new OrderSteps();
    private final Faker faker = new Faker();
    private String firstName;
    private String lastName;
    private String address;
    private ValidatableResponse firstOrder;
    private ValidatableResponse secondOrder;

    @Before
    public void setUp() {

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstName = faker.name().username();
        lastName = faker.internet().password();
        address = faker.name().name();

        firstOrder = orderSteps
                .createOrderWithColorBlack(firstName, lastName, address)
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());

        secondOrder = orderSteps
                .createOrderWithColorBlack(firstName, lastName, address)
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());
    }

    @Test
    @DisplayName("Test body response")
    @Description("Этот тест проверяет возврат в ответе номера заказа")
    public void testBodyResponse() { // тело ответа содержит track

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstOrder.statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());
    }

    @Test
    @DisplayName("Test list orders in body response")
    @Description("Этот тест проверяет возврат в ответе список заказов")
    public void testListOrdersInBodyResponse() { // в тело ответа возвращается список заказов

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

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