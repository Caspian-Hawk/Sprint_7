package sprintseven;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sprintseven.steps.CourierSteps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTests {

    private CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;
    private String firstName;

    @Test
    @DisplayName("Check possibility login courier")
    public void checkPossibilityLoginCourier() { // курьер может авторизоваться

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(201)
                .body("ok", is(true));

        courierSteps
                .loginCourier(login, password)
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Check login courier with all required fields")
    public void checkLoginCourierWithAllRequiredFields() { // для авторизации нужно передать все обязательные поля

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithAllRequiredFields(login, password, firstName)
                .statusCode(201)
                .body("ok", is(true));

        courierSteps
                .loginCourier(login, password)
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Check error wrong login")
    public void checkErrorWrongLogin() { // система вернёт ошибку, если неправильно указать логин или пароль

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithAllRequiredFields(login, password, firstName)
                .statusCode(201)
                .body("ok", is(true));

        login = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .loginCourier(login, password)
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check error without login")
    public void checkErrorWithoutLogin() { // если какого-то поля нет, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithAllRequiredFields(login, password, firstName)
                .statusCode(201)
                .body("ok", is(true));

        courierSteps
                .loginCourierWithoutLogin(password)
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа")); // если логиниться без логина выдает ошибку
    }

    @Test
    @DisplayName("Check error without password")
    public void checkErrorWithoutPassword() { // если какого-то поля нет, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithAllRequiredFields(login, password, firstName)
                .statusCode(201)
                .body("ok", is(true));

        courierSteps
                .loginCourierWithoutPassword(login)
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа")); // BUG, тут какие-то проблемы с сервером
    }

    @Test
    @DisplayName("Check error non existent login")
    public void checkErrorNonExistentLogin() { // если авторизоваться под несуществующим пользователем, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .loginCourier(login, password)
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check response id")
    public void checkResponseId() { // успешный запрос возвращает id

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(201)
                .body("ok", is(true));

        courierSteps
                .loginCourier(login, password)
                .statusCode(200)
                .body("id", notNullValue());
    }

    public void tearDown() {
        Integer id = courierSteps.loginCourier(login,password).extract().path("id");
        courierSteps.deleteCourier(id);
    }
}