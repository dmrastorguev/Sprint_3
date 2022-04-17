package ru.yandex.praktikum.courierCreate;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.CourierApiClient;
import ru.yandex.praktikum.model.CourierCreate;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(Parameterized.class)
public class CourierCreateWithNullFieldsTest {

    private CourierApiClient courierApiClient;
    private String courierLogin;
    private String courierPassword;
    private String courierFirstName;

    public CourierCreateWithNullFieldsTest(String courierLogin, String courierPassword, String courierFirstName) {
        this.courierLogin = courierLogin;
        this.courierPassword = courierPassword;
        this.courierFirstName = courierFirstName;
    }


    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"","ВилкинИложкин_2022","Василий"},
                {"ВилкинИложкин_2022","","Василий"},
                {"","","Василий"},
                {"ВилкинИложкин_2022","ВилкинИложкин_2022",""},
                {"ВилкинИложкин_2022","",""},
                {"","ВилкинИложкин_2022",""},
                {"","",""}
        };
    }

    @Test
    @DisplayName("Тест на невозможность создания курьера без одного из обязательных полей")
    @Description("Тест проверяет, что в одном из обязательных полей отсутствуют значения,то запрос возвращает ошибку.")
    @TmsLink("http://qa-scooter.praktikum-services.ru/docs/#api-Courier-CreateCourier")
    @Issue("Курьер создается, если в значении параметра FirstName передается null. Набор данных №4 возвращает статус 201")
    public void courierCreateWithNullFieldsTest() {

        ValidatableResponse courierCreateWithNullFieldsTestResponse = courierApiClient.create(new CourierCreate(courierLogin,courierPassword,courierFirstName));

        int statusCode = courierCreateWithNullFieldsTestResponse.extract().statusCode();
        String errorMessage = courierCreateWithNullFieldsTestResponse.extract().path("message");

        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Courier is created", errorMessage ,equalTo("Недостаточно данных для создания учетной записи"));
    }
}
