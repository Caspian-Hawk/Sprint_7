package sprintseven.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static sprintseven.steps.ApiEndpoints.ORDER;
import static sprintseven.steps.ApiEndpoints.ORDER_FINISH;

public class OrderSteps {

    @Step("Create order with multiple colors")
    public ValidatableResponse createOrderWithMultipleColors(String firstName, String lastName, String address) {
        String[] colors = new String[]{"BLACK", "GREY"};
        OrderModel order = new OrderModel(null, null, null, null, null, null, null, null, colors);

        return RestClient.getRequestSpecification()
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Create order with color black")
    public ValidatableResponse createOrderWithColorBlack(String firstName, String lastName, String address) {
        String[] colors = new String[]{"BLACK"};
        OrderModel order = new OrderModel(null, null, null, null, null, null, null, null, colors);
        return RestClient.getRequestSpecification()
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Get orders")
    public ValidatableResponse getOrders() {
        return RestClient.getRequestSpecification()
                .when()
                .get(ORDER).then();
    }

    @Step("Finish order")
    public ValidatableResponse finishOrder(int id) {
        return RestClient.getRequestSpecification()
                .pathParam("id", id)
                .when()
                .put(ORDER_FINISH)
                .then();
    }
}