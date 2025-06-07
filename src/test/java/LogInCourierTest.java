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

public class LogInCourierTest {
    private CourierClient courierClient;
    private String defaultCourierLogin;
    private String defaultCourierPassword;
    private String defaultCourierFirstName;
    private String courierId;

    @Before
    public void before() {
        defaultCourierFirstName = "имя_"  + System.currentTimeMillis();
        defaultCourierPassword = "пароль_" + System.currentTimeMillis();
        defaultCourierLogin = "логин_"  + System.currentTimeMillis();
        courierClient = new CourierClient();
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        // Сразу создаем курьера, по данным которого будем авторизовываться в рамках теста
        Courier courier = new Courier(defaultCourierLogin, defaultCourierPassword, defaultCourierFirstName);
        // Создаем курьера через api, сохраняем id для чистки после тестов
        courierId = courierClient.createCourierAndReturnResponse(courier).jsonPath().get("id");
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
    @DisplayName("Авторизация курьера с корректными данными")
    @Description("Проверка усрешной авторизации курьера с правильным логином и паролем")
    public void courierLoginSuccessTest() {
        // Создаем объект курьера для авторизации
        Courier courier = new Courier(
                defaultCourierLogin,
                defaultCourierPassword,
                defaultCourierFirstName
        );
        // Пробуем авторизоваться
        Response loginResponse = courierClient.logInCourierAndReturnResponse(courier);

        // Проверка успешной авторизации и получения ID курьера
        loginResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .and()
                .body("id", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Попытка авторизации без логина")
    @Description("Проверка обработки ошибки при авторизации курьера без логина")
    public void courierLoginWithoutLoginTest() {
        // Создаем курьера без логина
        Courier courierWithoutLogin = new Courier(
                "",
                defaultCourierPassword,
                defaultCourierFirstName
        );
        Response loginResponse = courierClient.logInCourierAndReturnResponse(courierWithoutLogin);

        // Проверка ошибки и соответствующего сообщения
        loginResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", Matchers.is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Попытка авторизации без пароля")
    @Description("Проверка обработки ошибки при авторизации уже созданного курьера без пароля")
    public void courierLoginWithoutPasswordTest() {
        // Создаем объект курьера без пароля
        Courier courierWithoutPassword = new Courier(
                defaultCourierLogin,
                "",
                defaultCourierFirstName
        );
        Response loginResponse = courierClient.logInCourierAndReturnResponse(courierWithoutPassword);

        // Проверка кода ответа и тела ответа
        loginResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", Matchers.is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Попытка авторизации с несуществующим логином")
    @Description("Проверка авторизации курьера с несуществующим логином")
    public void courierLoginWithNonExistentLoginTest() {
        // Используем несуществующий логин
        String loginNotExisted = "несуществующий_логин_" + System.currentTimeMillis();
        Courier courierWithInvalidLogin = new Courier(
                loginNotExisted,
                defaultCourierPassword,
                defaultCourierFirstName
        );
        Response loginResponse = courierClient.logInCourierAndReturnResponse(courierWithInvalidLogin);

        // Проверка кода ответа и тела ответа
        loginResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", Matchers.is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Попытка авторизации с некорректным логином")
    @Description("Проверка авторизации курьера с некорректно указанным логином")
    public void courierLoginWithIncorrectLoginTest() {
        // Указываем неправильный логин
        String incorrectLogin = defaultCourierLogin + System.currentTimeMillis();;
        Courier courierWithIncorrectLogin = new Courier(
                incorrectLogin,
                defaultCourierPassword,
                defaultCourierFirstName
        );
        Response loginResponse = courierClient.logInCourierAndReturnResponse(courierWithIncorrectLogin);

        // Проверка кода ответа и тела ответа
        loginResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", Matchers.is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Попытка авторизации с некорректным паролем")
    @Description("Проверка авторизации курьера с некорректно введенным паролем")
    public void courierLoginWithIncorrectPasswordTest() {
        // Указываем неправильный пароль
        String incorrectPassword = "неверный_пароль";
        Courier courierWithIncorrectPassword = new Courier(defaultCourierLogin, incorrectPassword, defaultCourierFirstName);
        Response loginResponse = courierClient.logInCourierAndReturnResponse(courierWithIncorrectPassword);

        // Проверка кода ответа и тела ответа
        loginResponse
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", Matchers.is("Учетная запись не найдена"));
    }
}
