import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateCourierTest {
    private CourierClient courierClient;
    private String courierId;
    String defaultCourierFirstName;
    String defaultCourierLogin;
    String defaultCourierPassword;

    @Before
    public void before() {
        defaultCourierFirstName = "имя_"  + System.currentTimeMillis();
        defaultCourierPassword = "пароль_" + System.currentTimeMillis();
        defaultCourierLogin = "логин_"  + System.currentTimeMillis();
        courierClient = new CourierClient();
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void after() {
        // Если у нас остался заполненным courierId, значит, данные требуют очистки
        if (courierId != null) {
            Response response = RestAssured
                    .given()
                    .when()
                    .delete(CourierClient.CREATE_COURIER_API_PATH + "/" + courierId);
            // А теперь проверяем код ответа, что удаление прошло успешно
            response
                    .then()
                    .log()
                    .all()
                    .assertThat()
                    .statusCode(200)
                    .and()
                    .body("ok", Matchers.is(true));
        }
    }

    @Test
    @DisplayName("Создание курьера с базовыми значениями")
    @Description("Проверка на успешное создание курьера с базовыми значениями и проверка кода ответа")
    public void createCourierBaseTest() {
        // Создаем сущность курьера со стандартными именем, логином и паролем
        Courier newCourier = new Courier(
                defaultCourierLogin,
                defaultCourierPassword,
                defaultCourierFirstName
        );
        // Создаем курьера через api
        Response createCourierResponse = courierClient.createCourierAndReturnResponse(newCourier);
        // Проверяем статус ответа
        createCourierResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(201)
                .and()
                .body("ok", Matchers.is(true));
        // И сохраняем id созданного курьера для последующей чистки
        courierId = createCourierResponse.jsonPath().get("id");
    }

    @Test
    @DisplayName("Создание курьера без опциональных полей")
    @Description("Проверка на успешное создание курьера без передачи имени и проверка кода ответа")
    public void createCourierWithoutFirstNameTest() {
        // Создаем сущность курьера со стандартными логином и паролем
        Courier newCourier = new Courier(
                defaultCourierLogin,
                defaultCourierPassword
        );
        // Создаем курьера через api
        Response createCourierResponse = courierClient.createCourierAndReturnResponse(newCourier);
        // Проверяем статус ответа
        createCourierResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(201)
                .and()
                .body("ok", Matchers.is(true));
        // И сохраняем id созданного курьера для последующей чистки
        courierId = createCourierResponse.jsonPath().get("id");
    }

    @Test
    @DisplayName("Создание двух курьеров с одинаковым логином")
    @Description("Проверка ответа при создании двух курьеров с одинаковым логином и проверка статуса ответа")
    public void createCourierWithDuplicateLoginTest() {
        // Создаем первого курьера с уникальным логином
        Courier firstCourier = new Courier(
                defaultCourierLogin,
                defaultCourierPassword,
                defaultCourierFirstName
        );
        Response firstCreateResponse = courierClient.createCourierAndReturnResponse(firstCourier);
        firstCreateResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(201)
                .and()
                .body("ok", Matchers.is(true));
        // Получаем ID первого курьера, записываем для последующей очистки
        courierId = firstCreateResponse.jsonPath().get("id");

        // Создаем второго курьера с тем же логином
        Courier secondCourier = new Courier(
                defaultCourierLogin,
                defaultCourierPassword,
                defaultCourierFirstName
        );
        Response secondCreateResponse = courierClient.createCourierAndReturnResponse(secondCourier);
        secondCreateResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(409)
                .and()
                .body("message", Matchers.is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Попытка создания курьера без логина")
    @Description("Проверка обработки ошибки при попытке создания курьера без указания логина и проверка статуса ответа")
    public void createCourierWithoutLoginTest() {
        // Создаем курьера без логина
        Courier courierWithoutLogin = new Courier(
                "",
                defaultCourierPassword,
                defaultCourierFirstName
        );
        Response createResponse = courierClient.createCourierAndReturnResponse(courierWithoutLogin);
        createResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Попытка создать курьера без указания пароля и проверка статуса ответа")
    public void createCourierWithoutPasswordTest() {
        // Создаем курьера без пароля
        Courier courierWithoutPassword = new Courier(
                defaultCourierLogin,
                "",
                defaultCourierFirstName
        );
        Response createResponse = courierClient.createCourierAndReturnResponse(courierWithoutPassword);
        createResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Попытка создания курьера без обязательных полей")
    @Description("Проверяем получение ошибки при попытке создания курьера без обязательных полей")
    public void createCourierWithoutRequiredFieldsTest() {
        // Создаем курьера без обязательных полей (логин и пароль)
        Courier courierWithoutRequiredFields = new Courier(
                "",
                "",
                ""
        );
        Response createResponse = courierClient.createCourierAndReturnResponse(courierWithoutRequiredFields);
        createResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }
}
