import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetOrdersTest {
    @Before
    public void before() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получить список заказов")
    @Description("Провперка получения списка заказов и кода ответа")
    public void getOrdersTest() {
        given()
                .header("Content-type", "application/json")
                .log()
                .all()
                .get("/api/v1/orders")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
