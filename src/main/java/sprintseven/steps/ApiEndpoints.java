package sprintseven.steps;

public class ApiEndpoints { // ручки
    public static final String COURIER = "/api/v1/courier";
    public static final String COURIER_LOGIN = COURIER + "/login";
    public static final String DELETE_COURIER = COURIER + "/{id}";
    public static final String ORDER = "/api/v1/orders";
    public static final String ORDER_FINISH = ORDER + "/finish/{id}";
}