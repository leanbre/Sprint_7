package client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import model.Courier;

import static io.restassured.RestAssured.given;

public class CourierClient {
    public static final String CREATE_COURIER_API_PATH = "/api/v1/courier";
    private static final String COURIER_LOG_IN_API_PATH = "/api/v1/courier/login";

    @Step("Создаем курьера")
    public Response createCourierAndReturnResponse(Courier courier) {
        return given()
                .log()
                .all()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER_API_PATH);
    }

    @Step("Проводим авторизацию курьера")
    public Response logInCourierAndReturnResponse(Courier courier) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_LOG_IN_API_PATH);
    }

    @Step("Удаляем курьера по id")
    public Response deleteCourierByIdAndReturnResponse(String courierId) {
        return given()
                .when()
                .delete(CREATE_COURIER_API_PATH + "/" + courierId);
    }
}
