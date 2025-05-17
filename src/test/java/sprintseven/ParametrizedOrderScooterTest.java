package sprintseven;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sprintseven.steps.OrderSteps;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class ParametrizedOrderScooterTest {
    private OrderSteps orderSteps = new OrderSteps();
    private final Faker faker = new Faker();
    private String firstName;
    private String lastName;
    private String address;
    private String[] color;
    private Integer orderId;

    public ParametrizedOrderScooterTest(String[] color) {
        this.color = color;
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
    @DisplayName("Test create order spectrum color")
    @Description("Этот тест проверяет возможность создания заказа со всеми возможными вариантами выбора цвета самоката")
    public void testCreateOrderSpectrumColor() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstName = faker.name().username();
        lastName = faker.internet().password();
        address = faker.name().name();

        // Создаем заказ и получаем ID
        orderId = orderSteps.createOrderWithMultipleColors(firstName, lastName, address)
                .statusCode(HttpStatus.SC_CREATED)
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