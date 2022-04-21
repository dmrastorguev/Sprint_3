package ru.yandex.praktikum.courierCreate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierApiClient;
import ru.yandex.praktikum.model.CourierCreate;
import ru.yandex.praktikum.model.CourierLogin;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;



public class CourierCreateWithoutFieldFirstNameTest {

    private CourierApiClient courierApiClient;
    private CourierCreate courierCreate;
    private int courierId;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        courierCreate = new CourierCreate("ВилкинИложкин_202299","ВилкинИложкин_202299","Василий");
    }

    @After
    public void tearDown()
    {
//     Вызываем метод для авторизации курьера (необходимо получить его id для последующего удаления)
        ValidatableResponse loginResponse = courierApiClient.login(new CourierLogin(courierCreate.getLogin(), courierCreate.getPassword()));
        courierId = loginResponse.extract().path("id");
//      удаляем курьера
        courierApiClient.delete(courierId);
    }

    @Test
    @DisplayName("Тест на проверку создание курьера без поля FirstName")
    @Description("Тест проверяет, что нельзя создать курьера без поля FirstName")
    @TmsLink("http://qa-scooter.praktikum-services.ru/docs/#api-Courier-CreateCourier")
    @Issue("Курьер создается без обязательного FirstName, возвращается статус 201")
    public void courierCreateWithoutFieldFirstName() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        CourierCreate courierCreateObject = new CourierCreate(courierCreate.getLogin(), courierCreate.getPassword(), null);
        String courierCreateConstructorWithoutSomeParameters = mapper.writeValueAsString(courierCreateObject);
        ValidatableResponse courierCreateWithoutFieldFirstName = courierApiClient.createWithoutSomeParameters(courierCreateConstructorWithoutSomeParameters);

        int statusCode = courierCreateWithoutFieldFirstName.extract().statusCode();
        String errorMessage = courierCreateWithoutFieldFirstName.extract().path("message");

        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Courier is created", errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
    }
}
