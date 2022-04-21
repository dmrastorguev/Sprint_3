package ru.yandex.praktikum.courierLogin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierApiClient;
import ru.yandex.praktikum.data.CourierGenerator;
import ru.yandex.praktikum.model.CourierLogin;
import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;



public class CourierLoginWithoutSomeFieldsTest {

    private CourierApiClient courierApiClient;
    private CourierGenerator courierGenerator;
    int courierId;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
    }

    @After
//      удаляем курьера
    public void tearDown() {
        courierApiClient.delete(courierId);
    }

    @Test
    @DisplayName("Тест на проверку авторизации курьера без поля Login")
    @Description("Тест проверяет, что нельзя авторизовать курьера без поля Login.Проверяется статус и сообщение об ошибке.")
    public void courierCreateWithoutFieldLogin() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

//      Подготавливаем данные. Создаем курьера, получаем данные его УЗ
        courierGenerator = new CourierGenerator();
        ArrayList<String> loginPassword = courierGenerator.getRandomCourierLogin();
        String login = loginPassword.get(0);
        String password = loginPassword.get(1);

//      Инициализируем CourierLogin с нулевым параметром Login
        CourierLogin courierLoginWithoutSomeParameters = new CourierLogin(null,password);

//      С помощью маппера Include.NON_NULL формируем для передачи данные, игнорируя поля с null
        String courierLoginConstructorWithoutSomeParameters = mapper.writeValueAsString(courierLoginWithoutSomeParameters);
        ValidatableResponse courierCreateWithoutFieldLogin = courierApiClient.createWithoutSomeParameters(courierLoginConstructorWithoutSomeParameters);

//      Получаем из ответа сервиса статус и сообщение об ошибке
        int statusCode = courierCreateWithoutFieldLogin.extract().statusCode();
        String errorMessage = courierCreateWithoutFieldLogin.extract().path("message");

//      Проверка статус и сообщение об ошибке на соответствие ожидаемому результату
        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Courier is created", errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест на проверку авторизации курьера без поля Password")
    @Description("Тест проверяет, что нельзя авторизовать курьера без поля Password.Проверяется статус и сообщение об ошибке.")
    public void courierCreateWithoutFieldPassword() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

//      Подготавливаем данные. Создаем курьера, получаем данные его УЗ
        courierGenerator = new CourierGenerator();
        ArrayList<String> loginPassword = courierGenerator.getRandomCourierLogin();
        String login = loginPassword.get(0);
        String password = loginPassword.get(1);

//      Инициализируем CourierLogin с нулевым параметром Password
        CourierLogin courierLoginWithoutSomeParameters = new CourierLogin(login,null);

//      С помощью маппера Include.NON_NULL формируем для передачи данные, игнорируя поля с null
        String courierLoginConstructorWithoutSomeParameters = mapper.writeValueAsString(courierLoginWithoutSomeParameters);
        ValidatableResponse courierCreateWithoutFieldPassword = courierApiClient.createWithoutSomeParameters(courierLoginConstructorWithoutSomeParameters);

//      Получаем из ответа сервиса статус и сообщение об ошибке
        int statusCode = courierCreateWithoutFieldPassword.extract().statusCode();
        String errorMessage = courierCreateWithoutFieldPassword.extract().path("message");

//      Проверка статус и сообщение об ошибке на соответствие ожидаемому результату
        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Courier is created", errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Тест на проверку авторизации курьера без полей Login и Password")
    @Description("Тест проверяет, что нельзя авторизовать курьера без полей Login и Password.Проверяется статус и сообщение об ошибке.")
    public void courierCreateWithoutFields() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

//      Подготавливаем данные. Создаем курьера, получаем данные его УЗ
        courierGenerator = new CourierGenerator();
        ArrayList<String> loginPassword = courierGenerator.getRandomCourierLogin();
        String login = loginPassword.get(0);
        String password = loginPassword.get(1);

//      Инициализируем CourierLogin с нулевым параметром Password
        CourierLogin courierLoginWithoutSomeParameters = new CourierLogin(null,null);

//      С помощью маппера Include.NON_NULL формируем для передачи данные, игнорируя поля с null
        String courierLoginConstructorWithoutSomeParameters = mapper.writeValueAsString(courierLoginWithoutSomeParameters);
        ValidatableResponse courierCreateWithoutFields = courierApiClient.createWithoutSomeParameters(courierLoginConstructorWithoutSomeParameters);

//      Получаем из ответа сервиса статус и сообщение об ошибке
        int statusCode = courierCreateWithoutFields.extract().statusCode();
        String errorMessage = courierCreateWithoutFields.extract().path("message");

//      Проверка статус и сообщение об ошибке на соответствие ожидаемому результату
        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Courier is created", errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
    }

}
