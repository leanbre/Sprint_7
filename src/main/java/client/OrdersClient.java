package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Orders;

import static io.restassured.RestAssured.given;

public class OrdersClient {
    private static final String CREATE_ORDER_API_PATH = "/api/v1/orders";

    @Step("Создаем заказы")
    public Response createOrdersAndReturnResponse(Orders orders) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .body(orders)
                .when()
                .post(CREATE_ORDER_API_PATH);
    }

    @Step("Получаем заказы")
    public Response getOrdersAndReturnResponse() {
        return given()
                .header("Content-type", "application/json")
                .log()
                .all()
                .get("/api/v1/orders");
    }
}
