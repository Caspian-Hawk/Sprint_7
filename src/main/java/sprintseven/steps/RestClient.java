package sprintseven.steps;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class RestClient {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    public static RequestSpecification getRequestSpecification() {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .contentType("application/json");
    }
}