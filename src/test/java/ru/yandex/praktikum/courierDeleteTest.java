package ru.yandex.praktikum;

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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;



public class courierDeleteTest {

    private CourierApiClient courierApiClient;
    CourierCreate courierCreate;
    Boolean isDeleteOk;
    int courierId;

    @Before
    public  void setUp(){
//      Создание курьера
        courierApiClient = new CourierApiClient();
        courierCreate = new CourierCreate(   "ВилкинИложкин_2022","ВилкинИложкин_2022","Василий");
        courierApiClient.create(new CourierCreate(courierCreate.getLogin(), courierCreate.getPassword(), courierCreate.getFirstName()));

//      Получение id курьера (авторизация курьера в системе)
        ValidatableResponse loginResponse = courierApiClient.login(new CourierLogin (courierCreate.getLogin(), courierCreate.getPassword()));
        courierId = loginResponse.extract().path("id");
    }


    @After
    public void tearDown(){
//  Удаляем курьера
        courierApiClient.delete(courierId);
    }

    @Test
    @DisplayName("Тест на проверку удаления курьера по Id")
    @Description("Удаление курьера по Id, проверка статуса и тела ответа")
    public void courierDeleteCorrectParameters() {
        ValidatableResponse courierDeleteResponse = courierApiClient.delete(courierId);

        int statusCode = courierDeleteResponse.extract().statusCode();
        isDeleteOk = courierDeleteResponse.extract().path("ok");

        assertThat("Status code couldn't be 200", statusCode, equalTo(SC_OK));
        assertThat("Courier is not Deleted", isDeleteOk ,is(true));
    }


    @Test
    @DisplayName("Тест на проверку удаления курьера с несуществующим Id")
    @Description("Удаление курьера с несуществующим Id, проверка статуса и тела ответа")
    public void courierDeleteWithWrongId() {
        int courierId =0;
        ValidatableResponse courierDeleteWithWrongIdResponse = courierApiClient.delete(courierId);


        int statusCode = courierDeleteWithWrongIdResponse.extract().statusCode();
        String errorMessage = courierDeleteWithWrongIdResponse.extract().path("message");

        assertThat("Status code couldn't be 404", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Wrong body message", errorMessage ,equalTo("Курьера с таким id нет."));
    }


    @Test
    @DisplayName("Тест на проверку удаления курьера без Id")
    @Description("Удаление курьера без Id, проверка статуса и тела ответа")
    @TmsLink("http://qa-scooter.praktikum-services.ru/docs/#api-Courier-DeleteCourier")
    @Issue("При удалении курьера без Id, возвращается некорректный статус ошибки 404 вместо 400")
    public void courierDeleteWithoutId() {

        ValidatableResponse courierDeleteWithoutIdResponse = courierApiClient.deleteDeleteWithoutId();

        int statusCode = courierDeleteWithoutIdResponse.extract().statusCode();
        String errorMessage = courierDeleteWithoutIdResponse.extract().path("message");

        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Wrong body message", errorMessage ,equalTo("Недостаточно данных для удаления курьера"));
    }
}
