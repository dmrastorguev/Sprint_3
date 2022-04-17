package ru.yandex.praktikum.courierCreate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierApiClient;
import ru.yandex.praktikum.model.CourierCreate;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;



public class CourierCreateWithoutSomeFieldsTest {

    private CourierApiClient courierApiClient;
    private CourierCreate courierCreate;
    private int courierId;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        courierCreate = new CourierCreate("ВилкинИложкин_202299","ВилкинИложкин_202299","Василий");
    }

    @Test
    @DisplayName("Тест на проверку создания курьера без поля Password")
    @Description("Тест проверяет, что нельзя создать курьера без поля Password")
    public void courierCreateWithoutFieldPassword() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        CourierCreate courierCreateObject = new CourierCreate(courierCreate.getLogin(), null, courierCreate.getFirstName());
        String courierCreateConstructorWithoutSomeParameters = mapper.writeValueAsString(courierCreateObject);

        ValidatableResponse courierCreateWithoutFieldLoginResponse = courierApiClient.createWithoutSomeParameters(courierCreateConstructorWithoutSomeParameters );

        int statusCode = courierCreateWithoutFieldLoginResponse.extract().statusCode();
        String errorMessage = courierCreateWithoutFieldLoginResponse.extract().path("message");

        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Courier is created", errorMessage, equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Тест на проверку создание курьера без поля Login")
    @Description("Тест проверяет, что нельзя создать курьера без поля Login")
    public void courierCreateWithoutFieldLogin() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        CourierCreate courierCreateObject = new CourierCreate( null, courierCreate.getPassword(), courierCreate.getFirstName());
        String courierCreateConstructorWithoutSomeParameters  = mapper.writeValueAsString(courierCreateObject);

        ValidatableResponse  courierCreateWithoutFieldLogin = courierApiClient.createWithoutSomeParameters(courierCreateConstructorWithoutSomeParameters );

        int statusCode = courierCreateWithoutFieldLogin.extract().statusCode();
        String errorMessage = courierCreateWithoutFieldLogin.extract().path("message");

        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Courier is created", errorMessage ,equalTo("Недостаточно данных для создания учетной записи"));

    }
}

