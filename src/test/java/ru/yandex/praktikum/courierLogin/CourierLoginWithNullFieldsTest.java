package ru.yandex.praktikum.courierLogin;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.CourierApiClient;
import ru.yandex.praktikum.model.CourierLogin;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CourierLoginWithNullFieldsTest {

    private CourierApiClient courierApiClient;
    private String courierLogin;
    private String courierPassword;

    public CourierLoginWithNullFieldsTest(String courierLogin, String courierPassword) {
        this.courierLogin = courierLogin;
        this.courierPassword = courierPassword;
    }

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
    }


    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"","ВилкинИложкин_2022"},
                {"ВилкинИложкин_2022",""},
                {"",""}
        };
    }

    @Test
    @DisplayName("Тест на невозможность авторизации курьера без одного из обязательных полей")
    @Description("Тест проверяет, что если одного из полей нет,то запрос возвращает ошибку.Все поля метода  авторизации являются обязательными")
    public void courierLoginWithNullFieldsTest() {

        ValidatableResponse courierLoginWithNullFieldsResponse = courierApiClient.login(new CourierLogin(courierLogin,courierPassword));

        int statusCode = courierLoginWithNullFieldsResponse.extract().statusCode();
        String errorMessage = courierLoginWithNullFieldsResponse.extract().path("message");

        assertThat("Status code couldn't be 409", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Login in system successfully", errorMessage ,equalTo("Недостаточно данных для входа"));
    }

}
