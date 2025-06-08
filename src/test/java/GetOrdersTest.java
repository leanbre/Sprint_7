import client.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class GetOrdersTest {
    private OrdersClient ordersClient;

    @Before
    public void before() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        ordersClient = new OrdersClient();
    }

    @Test
    @DisplayName("Получить список заказов")
    @Description("Провперка получения списка заказов и кода ответа")
    public void getOrdersTest() {
        Response getOrdersReponse = ordersClient.getOrdersAndReturnResponse();
        getOrdersReponse
                .then()
                .assertThat()
                .statusCode(200);
    }
}
