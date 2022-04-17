package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.OrderApiClient;
import ru.yandex.praktikum.model.OrderCancel;
import ru.yandex.praktikum.model.OrderCreate;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;



@RunWith(Parameterized.class)
public class OrderCreateTest {

    private OrderApiClient orderApiClient;
    int orderTrack;


    private  String  firstName;
    private  String  address;
    private  String  metroStation;
    private  String  phone;
    private  int rentTime;
    private  String  deliveryDate;
    private  String  comment;
    private  String[]  color;

    public OrderCreateTest(String firstName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }


    @Before
    public void setUp() {
        orderApiClient = new OrderApiClient();
    }

    @After
    public void tearDown(){
//      Пробуем отменить, созданные заказы, но ручка /api/v1/orders/cancel не работает
        ValidatableResponse orderCancel = orderApiClient.cancelOrderApi(new OrderCancel(orderTrack));
        System.out.println(orderCancel.extract().response().getBody().print());

    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
//               Проверь, что когда создаёшь заказ:можно указать один из цветов — BLACK или GREY;
                {"Иннокентий", "г.Москва, ул.8-Марта", "4", "+74951002030", 5, "2022-04-10", "Гениальнo! Слушайте, я не узнаю вас в гриме.Бoже мoй, Иннoкентий Смoктунoвский!Кеша!", new String[]{"BLACK"}},
//               Проверь, что когда создаёшь заказ:можно указать один из цветов — BLACK или GREY;
                {"Иннокентий", "г.Москва, ул.8-Марта", "4", "+74951002030", 5, "2022-04-10", "Гениальнo! Слушайте, я не узнаю вас в гриме.Бoже мoй, Иннoкентий Смoктунoвский!Кеша!", new String[]{"GRAY"}},
//               Проверь, что когда создаёшь заказ:можно указать оба цвета;
                {"Иннокентий", "г.Москва, ул.8-Марта", "4", "+74951002030", 5, "2022-04-10", "Гениальнo! Слушайте, я не узнаю вас в гриме.Бoже мoй, Иннoкентий Смoктунoвский!Кеша!", new String[]{"BLACK","GRAY"}},
//               Проверь, что когда создаёшь заказ:можно совсем не указывать цвет;
                {"Иннокентий", "г.Москва, ул.8-Марта", "4", "+74951002030", 5, "2022-04-10", "Гениальнo! Слушайте, я не узнаю вас в гриме.Бoже мoй, Иннoкентий Смoктунoвский!Кеша!", new String[]{""}}
        };
    }


    @Test
    @DisplayName("Тест на проверку создание заказа со всеми параметрами")
    @Description("Тест проверяет создание заказа со всеми параметрами, проверят статус и тело ответа")
    public void orderCreateWithCorrectParameters() {

        ValidatableResponse orderCreateWithCorrectParametersResponse = orderApiClient.createOrderApi(new OrderCreate(firstName, address, metroStation, phone, rentTime,  deliveryDate, comment, color));

        int statusCode = orderCreateWithCorrectParametersResponse.extract().statusCode();
        orderTrack = orderCreateWithCorrectParametersResponse.extract().path("track");

        assertThat("Status code couldn't be 201", statusCode, equalTo(SC_CREATED));
        assertThat("Order is not created", orderTrack , is(not(0)));
    }
}
