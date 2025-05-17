package sprintseven;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.http.HttpStatus;
import org.junit.Test;
import sprintseven.steps.CourierSteps;
import static org.hamcrest.Matchers.is;

public class CreateCourierTests {

    private CourierSteps courierSteps = new CourierSteps();
    private final Faker faker = new Faker();
    private String login;
    private String password;
    private String firstName;

    @Test
    @DisplayName("Test possibility create courier")
    @Description("Этот тест проверяет что можно создать курьера")
    public void testPossibilityCreateCourier() { // можно создать курьера

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = faker.name().username();
        password = faker.internet().password();

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(HttpStatus.SC_CREATED) // statusCode(201)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Test not create double courier")
    @Description("Этот тест проверяет что нельзя создать двух одинаковых курьеров")
    public void testNotCreateDoubleCourier() { // нельзя создать двух одинаковых курьеров

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = faker.name().username();
        password = faker.internet().password();

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", is("Этот логин уже используется. Попробуйте другой.")); // ответ в документации не соответствует фактическому
    }

    @Test
    @DisplayName("Test create courier with all required fields")
    @Description("Этот тест проверяет что можно создать курьера со всеми заполненными полями")
    public void testCreateCourierWithAllRequiredFields() { // чтобы создать курьера, нужно передать в ручку все обязательные поля

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
    @DisplayName("Test correct code")
    @Description("Этот тест проверяет что при создании валидного курьера возвращается код 201")
    public void testCorrectCode() { // запрос возвращает правильный код ответа

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = faker.name().username();
        password = faker.internet().password();

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Test body error create courier without password")
    @Description("Этот тест проверяет что при создании курьера без поля password запрос возвращает ошибку \"Недостаточно данных для создания учетной записи\"")
    public void testBodyErrorCreateCourierWithoutPassword() { // если одного из полей нет, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = faker.name().username();

        courierSteps
                .createCourierWithoutPasswordAndFirstName(login)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Test body error create courier without login")
    @Description("Этот тест проверяет что при создании курьера без поля login запрос возвращает ошибку \"Недостаточно данных для создания учетной записи\"")
    public void testBodyErrorCreateCourierWithoutLogin() { // если одного из полей нет, запрос возвращает ошибку

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        password = faker.internet().password();

        courierSteps
                .createCourierWithoutPasswordAndFirstName(password)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Test body error impossible create double courier")
    @Description("Тест проверяет что при создании уже существующего курьера возвращается ошибка \"Этот логин уже используется\"")
    public void testBodyErrorImpossibleCreateDoubleCourier() { // если создать пользователя с логином, который уже есть, возвращается ошибка

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = faker.name().username();
        password = faker.internet().password();

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));

        courierSteps
                .createCourierWithoutFirstName(login, password)
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", is("Этот логин уже используется")); // BUG, ответ в документации не соответствует фактическому
    }

    public void tearDown() {
        Integer id = courierSteps.loginCourier(login,password).extract().path("id");
        courierSteps.deleteCourier(id);
    }
}