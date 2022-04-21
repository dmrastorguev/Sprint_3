package ru.yandex.praktikum.courierCreate;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierApiClient;
import ru.yandex.praktikum.model.CourierCreate;
import ru.yandex.praktikum.model.CourierLogin;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class CourierCreateTest {

    private CourierApiClient courierApiClient;
    private CourierCreate courierCreate;
    int courierId;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        courierCreate = new CourierCreate("ВилкинИложкин_2022", "ВилкинИложкин_2022", "Василий");
    }

    @After
    public void tearDown() {
//     Вызываем метод для авторизации курьера (необходимо получить его id для последующего удаления)
        ValidatableResponse loginResponse = courierApiClient.login(new CourierLogin(courierCreate.getLogin(), courierCreate.getPassword()));
        courierId = loginResponse.extract().path("id");
//      удаляем курьера
        courierApiClient.delete(courierId);
    }


    @Test
    @DisplayName("Тест на проверку создание курьера")
    @Description("Тест на проверку создание курьера с корректными параметрами")
    public void courierCreateWithCorrectParameter() {

        ValidatableResponse courierCreateResponse = courierApiClient.create(new CourierCreate(courierCreate.getLogin(), courierCreate.getPassword(), courierCreate.getFirstName()));
//        System.out.println(courierCreateResponse.extract().response().getBody().print());

        int statusCode = courierCreateResponse.extract().statusCode();
        Boolean isLoginOk = courierCreateResponse.extract().path("ok");

        assertThat("Status code couldn't be 201", statusCode, equalTo(SC_CREATED));
        assertThat("Courier is not created", isLoginOk, is(true));

    }


    @Test
    @DisplayName("Тест на проверку создание дубликата курьера")
    @Description("Тест проверяет, что нельзя создать двух одинаковых курьеров")
    public void courierCreateDuplicateRecord() {
        courierApiClient.create(new CourierCreate(courierCreate.getLogin(), courierCreate.getPassword(), courierCreate.getFirstName()));
        ValidatableResponse courierCreateDuplicateRecord = courierApiClient.create(new CourierCreate(courierCreate.getLogin(), courierCreate.getPassword(), courierCreate.getFirstName()));

        int statusCode = courierCreateDuplicateRecord.extract().statusCode();
        String errorMessage = courierCreateDuplicateRecord.extract().path("message");

        assertThat("Status code couldn't be 409", statusCode, equalTo(SC_CONFLICT));
        assertThat("Courier is created", errorMessage, equalTo("Этот логин уже используется. Попробуйте другой."));
    }


    @Test
    @DisplayName("Тест на проверку создание курьера с логином, который уже зарегистрирован в системе.")
    @Description("Тест проверяет,что если попробовать создать курьера с логином, который уже есть,то возвращается ошибка.")
    public void courierCreateDuplicateLogin() {
        String courierFirstNameNew = RandomStringUtils.randomAlphabetic(10);
        String courierPasswordNew = RandomStringUtils.randomAlphabetic(10);

        courierApiClient.create(new CourierCreate(courierCreate.getLogin(), courierCreate.getPassword(), courierCreate.getFirstName()));
        ValidatableResponse createDuplicateLoginRecord = courierApiClient.create(new CourierCreate(courierCreate.getLogin(), courierPasswordNew, courierFirstNameNew));

        int statusCode = createDuplicateLoginRecord.extract().statusCode();
        String errorMessage = createDuplicateLoginRecord.extract().path("message");

        assertThat("Status code couldn't be 409", statusCode, equalTo(SC_CONFLICT));
        assertThat("Courier is created", errorMessage, equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}


