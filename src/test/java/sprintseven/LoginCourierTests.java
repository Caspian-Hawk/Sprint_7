package sprintseven;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import sprintseven.steps.CourierSteps;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTests {

    private CourierSteps courierSteps = new CourierSteps();
    private final Faker faker = new Faker();
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() { // создания курьера
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = faker.name().username();
        password = faker.internet().password();
        firstName = faker.name().firstName();

        courierSteps
                .createCourierWithAllRequiredFields(login, password, firstName)
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Test possibility login courier")
    @Description("Этот тест проверяет что можно авторизовать курьера")
    public void testPossibilityLoginCourier() { // курьер может авторизоваться

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        courierSteps
                .loginCourier(login, password)
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Test error wrong login")
    @Description("Этот тест проверяет что система вернёт ошибку \"Учетная запись не найдена\", если неправильно указать login или password")
    public void testErrorWrongLogin() { // система вернёт ошибку, если неправильно указать логин или пароль

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = faker.name().username();

        courierSteps
                .loginCourier(login, password)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Test error without login")
    @Description("Этот тест проверяет что запрос вернёт ошибку \"Недостаточно данных для входа\", если не указывать login при авторизации")
    public void testErrorWithoutLogin() { // если какого-то поля нет, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        courierSteps
                .loginCourierWithoutLogin(password)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа")); // если логиниться без логина выдает ошибку
    }

    @Test
    @DisplayName("Test error without password")
    @Description("Этот тест проверяет что запрос вернёт ошибку \"Недостаточно данных для входа\", если не указывать password при авторизации")
    public void testErrorWithoutPassword() { // если какого-то поля нет, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        courierSteps
                .loginCourierWithoutPassword(login)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа")); // BUG, тут какие-то проблемы с сервером
    }

    @Test
    @DisplayName("Test error non existent login")
    @Description("Этот тест проверяет если авторизоваться под несуществующим пользователем, запрос возвращает ошибку \"Учетная запись не найдена\"")
    public void testErrorNonExistentLogin() { // если авторизоваться под несуществующим пользователем, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = faker.name().username();
        password = faker.internet().password();
        firstName = faker.name().firstName();

        courierSteps
                .loginCourier(login, password)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    public void tearDown() {
        Integer id = courierSteps.loginCourier(login,password).extract().path("id");
        courierSteps.deleteCourier(id);
    }
}