import client.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Orders;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {
    private final Orders orders;
    private final OrdersClient ordersClient;

    public CreateOrderParametrizedTest(Orders orders) {
        this.orders = orders;
        this.ordersClient = new OrdersClient();
    }

    @Before
    public void before() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters(name = "Создание тестовых данных: {0}")
    public static Object[][] createTestData() {
        return new Object[][] {
                {new Orders("2025-06-01", "Я звезда, мой адрес - загадка", "Играл в кино, какое метро", 1, new String[]{"BLACK"}, "123456", "Педро", "Антуанеттов", "Заказ с цветом BLACK")},
                {new Orders("2025-06-02", "Тоже звезда, но победнее", "Zaragoza", 2, new String[]{"GREY"}, "654321", "Хуан", "Иванов", "Заказ с цветом GREY")},
                {new Orders("2025-06-03", "CDA DE MOCTEZUMA NO. 39, LA HABANA, 13050", "Tacubaya", 3, new String[]{"BLACK", "GREY"}, "0987654321", "Тест", "Тестов", "Заказ с цветами BLACK и GREY")},
                {new Orders("2025-06-03", "CDA DE MOCTEZUMA NO. 39, LA HABANA, 13050", "Zaragoza", 4, new String[]{}, "09876543210", "Тестита", "Тестова", "Заказ, где цвета нет")}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Параметризированный тест на создание заказа с разными тест-кейсами")
    public void createOrderParametrizedTest() {
        Response ordersCreatedResponse = ordersClient.createOrdersAndReturnResponse(orders);
        ordersCreatedResponse
                .then()
                .log()
                .all()
                .assertThat()
                .and()
                .statusCode(201)
                .body("track", Matchers.notNullValue()
        );
    }
}
