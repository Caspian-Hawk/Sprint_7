package sprintseven.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static sprintseven.steps.ApiEndpoints.*;

public class CourierSteps {

    @Step("Create courier with all required fields")
    public ValidatableResponse createCourierWithAllRequiredFields(String login, String password, String firstName) {
        CourierModel courier = new CourierModel(login, password, firstName);
        return RestClient.getRequestSpecification()
                .body(courier)
                .when()
                .post(COURIER)
                .then();
    }

    @Step("Create courier without parameter firstName")
    public ValidatableResponse createCourierWithoutFirstName(String login, String password) {
        CourierModel courier = new CourierModel(login, password, null);
        return RestClient.getRequestSpecification()
                .body(courier)
                .when()
                .post(COURIER)
                .then();
    }

    @Step("Create courier without parameters password and firstName")
    public ValidatableResponse createCourierWithoutPasswordAndFirstName(String login) {
        CourierModel courier = new CourierModel(login, null, null);
        return RestClient.getRequestSpecification()
                .body(courier)
                .when()
                .post(COURIER)
                .then();
    }

    @Step("Create courier without parameters password and firstName")
    public ValidatableResponse createCourierWithoutLoginAndFirstName(String password) {
        CourierModel courier = new CourierModel(null, password, null);
        return RestClient.getRequestSpecification()
                .body(courier)
                .when()
                .post(COURIER)
                .then();
    }

    @Step("Login courier")
    public ValidatableResponse loginCourier(String login, String password) {
        CourierModel courier = new CourierModel(login, password, null);
        return RestClient.getRequestSpecification()
                .body(courier)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step("Login courier without login")
    public ValidatableResponse loginCourierWithoutLogin(String password) {
        CourierModel courier = new CourierModel(null, password, null);
        return RestClient.getRequestSpecification()
                .body(courier)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step("Login courier without parameter password")
    public ValidatableResponse loginCourierWithoutPassword(String login) {
        CourierModel courier = new CourierModel(login, null, null);
        return RestClient.getRequestSpecification()
                .body(courier)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step("Delete courier")
    public ValidatableResponse deleteCourier(int id) {
        return RestClient.getRequestSpecification()
                .pathParam("id", id)
                .when()
                .delete(DELETE_COURIER)
                .then();
    }
}