package sprintseven;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sprintseven.steps.CourierSteps;

import static org.hamcrest.Matchers.is;

public class CreateCourierTests {

    private CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;
    private String firstName;

    @Test
    @DisplayName("Check possibility create courier")
    public void checkPossibilityCreateCourier() { // можно создать курьера

//        RestAssured.config = RestAssured.config()
//                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Check not create double courier")
    public void checkNotCreateDoubleCourier() { // нельзя создать двух одинаковых курьеров

//        RestAssured.config = RestAssured.config()
//                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(201)
                .body("ok", is(true));

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(409)
                .body("message", is("Этот логин уже используется. Попробуйте другой.")); // ответ в документации не соответствует фактическому
    }

    @Test
    @DisplayName("Check create courier with all required fields")
    public void checkCreateCourierWithAllRequiredFields() { // чтобы создать курьера, нужно передать в ручку все обязательные поля

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithAllRequiredFields(login, password, firstName)
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Check correct code")
    public void checkCorrectCode() { // запрос возвращает правильный код ответа

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Check body response")
    public void checkBodyResponse() { // успешный запрос возвращает ok: true

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Check body error create invalid courier")
    public void checkBodyErrorCreateInvalidCourier() { // если одного из полей нет, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithoutPasswordAndFirstName(login)
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check Body Error Impossible Create Double Courier")
    public void checkBodyErrorImpossibleCreateDoubleCourier() { // если создать пользователя с логином, который уже есть, возвращается ошибка

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(201)
                .body("ok", is(true));

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(409)
                .body("message", is("Этот логин уже используется")); // BUG, ответ в документации не соответствует фактическому
    }

    public void tearDown() {
        Integer id = courierSteps.loginCourier(login,password).extract().path("id");
        courierSteps.deleteCourier(id);
    }
}