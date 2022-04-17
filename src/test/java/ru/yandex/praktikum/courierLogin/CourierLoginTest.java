package ru.yandex.praktikum.courierLogin;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierApiClient;
import ru.yandex.praktikum.data.CourierGenerator;
import ru.yandex.praktikum.model.CourierLogin;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;



public class CourierLoginTest {

    private CourierApiClient courierApiClient;
    private CourierGenerator courierGenerator;
    int courierId;

    @Before
    public  void setUp(){
        courierApiClient = new CourierApiClient();
    }

    @After
//      удаляем курьера
    public void tearDown(){
        courierApiClient.delete(courierId);
    }

    @Test
    @DisplayName("Тест на проверку возможности логина курьера ") // имя теста
    @Description("Проверяется возможность логина курьера,также проверяется статус и тело ответа") // описание теста
    public void courierLoginCorrectParameters() {

        courierGenerator = new CourierGenerator();
        ArrayList<String> loginPassword = courierGenerator.getRandomCourierLogin();
        String login = loginPassword.get(0);
        String password = loginPassword.get(1);

        ValidatableResponse loginResponse = courierApiClient.login(new CourierLogin (login, password));
        int statusCode = loginResponse.extract().statusCode();
        courierId =  loginResponse.extract().path("id");

        assertThat("Status code couldn't be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }

    @Test
    @DisplayName("Тест на проверку возможности логина курьера c некорректным логином") // имя теста
    @Description("Проверяется возможность логина курьера c некорректным логином, а также проверка статуса и тела ответа") // описание теста
    public void courierLoginIncorrectLogin() {

        courierGenerator = new CourierGenerator();
        ArrayList<String> loginPassword = courierGenerator.getRandomCourierLogin();
        String login = loginPassword.get(0)+770077;
        String password = loginPassword.get(1);

        ValidatableResponse loginResponse = courierApiClient.login(new CourierLogin (login, password));
        int statusCode = loginResponse.extract().statusCode();
        String errorMessage =  loginResponse.extract().path("message");

        assertThat("Status code couldn't be 404", statusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Wrong message body", errorMessage,equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Тест на проверку возможности логина курьера c некорректным паролем") // имя теста
    @Description("Проверяется возможность логина курьера c некорректным c некорректным паролем а также проверка статуса и тела ответа") // описание теста
    public void courierLoginIncorrectPassword() {

        courierGenerator = new CourierGenerator();
        ArrayList<String> loginPassword = courierGenerator.getRandomCourierLogin();
        String login = loginPassword.get(0);
        String password = loginPassword.get(1)+770077;

        ValidatableResponse loginResponse = courierApiClient.login(new CourierLogin (login, password));
        int statusCode = loginResponse.extract().statusCode();
        String errorMessage =  loginResponse.extract().path("message");

        assertThat("Status code couldn't be 404", statusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Wrong message body", errorMessage,equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Тест на проверку возможности логина незарегистрированного курьера") // имя теста
    @Description("Проверяется, что если авторизоваться под несуществующим пользователем, запрос возвращает ошибку") // описание теста
    public void courierLoginWithNotExistCourier() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        courierGenerator = new CourierGenerator();

        ValidatableResponse loginResponse = courierApiClient.login(new CourierLogin (login, password));
        int statusCode = loginResponse.extract().statusCode();
        String errorMessage =  loginResponse.extract().path("message");

        assertThat("Status code couldn't be 404", statusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat("Wrong message body", errorMessage,equalTo("Учетная запись не найдена"));
    }

}
