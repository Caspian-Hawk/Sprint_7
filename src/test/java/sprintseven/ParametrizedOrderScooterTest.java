package sprintseven;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sprintseven.steps.OrderSteps;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class ParametrizedOrderScooterTest {
    private OrderSteps orderSteps = new OrderSteps();
    private String firstName;
    private String lastName;
    private String address;
    private String[] colors;
    private Integer orderId;

    public ParametrizedOrderScooterTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] colors() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}} // пустой массив
        };
    }

    @Test
    @DisplayName("Check create order spectrum color")
    public void checkCreateOrderSpectrumColor() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstName = RandomStringUtils.randomAlphabetic(10);
        lastName = RandomStringUtils.randomAlphabetic(10);
        address = RandomStringUtils.randomAlphabetic(10);

        // Создаем заказ и получаем ID
        orderId = orderSteps.createOrderWithMultipleColors(firstName, lastName, address, colors)
                .statusCode(201)
                .body("track", notNullValue())
                .extract()
                .path("track");
    }

    @After
    public void tearDown() {
        // Завершение заказа, если ID был получен
        if (orderId != null) {
            orderSteps.finishOrder(orderId);
        }
    }
}