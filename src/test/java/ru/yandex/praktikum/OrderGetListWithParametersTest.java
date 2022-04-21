package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierApiClient;
import ru.yandex.praktikum.client.OrderApiClient;
import ru.yandex.praktikum.data.CourierGenerator;
import ru.yandex.praktikum.model.CourierCreate;
import ru.yandex.praktikum.model.CourierLogin;
import ru.yandex.praktikum.model.OrderCancel;
import ru.yandex.praktikum.model.OrderCreate;

import java.util.ArrayList;
import java.util.HashMap;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;




public class OrderGetListWithParametersTest {
    //  Инициализация методов и переменных для курьера
    private CourierApiClient courierApiClient;
    private CourierCreate courierCreate;
    private CourierGenerator courierGenerator;
    int courierId;

    //  Инициализация методов и переменных для заказа
    private OrderApiClient orderApiClient;
    private OrderCreate orderCreate;
    int orderTrack;
    int orderId;

    @Before
    public void setUp() {
//      Создание курьера
        courierApiClient = new CourierApiClient();
        courierCreate = new CourierCreate("ВилкинИложкин_2022", "ВилкинИложкин_2022", "Василий");
        courierApiClient.create(new CourierCreate(courierCreate.getLogin(), courierCreate.getPassword(), courierCreate.getFirstName()));

//      Получение id курьера (авторизация курьера в системе)
        ValidatableResponse loginResponse = courierApiClient.login(new CourierLogin(courierCreate.getLogin(), courierCreate.getPassword()));
        courierId = loginResponse.extract().path("id");

//      Создание заказа
        orderApiClient = new OrderApiClient();
        orderCreate = new OrderCreate("Иннокентий", "г.Москва, ул.8-Марта", "4", "+74951002030", 5, "2022-04-10", "Гениальнo! Слушайте, я не узнаю вас в гриме.Бoже мoй, Иннoкентий Смoктунoвский!Кеша!", new String[]{"BLACK"});
        ValidatableResponse orderCreateWithCorrectParametersResponse = orderApiClient.createOrderApi(new OrderCreate(orderCreate.getFirstName(), orderCreate.getAddress(), orderCreate.getMetroStation(), orderCreate.getPhone(), orderCreate.getRentTime(), orderCreate.getDeliveryDate(), orderCreate.getComment(), orderCreate.getColor()));
        orderTrack = orderCreateWithCorrectParametersResponse.extract().path("track");

//      Получить заказ по трекинговому номеру заказа(track) для получения его id
        ValidatableResponse orderGetByIdResponse = orderApiClient.getOrderByIdApi(orderTrack);
        orderId = orderGetByIdResponse.extract().path("order.id");

//      Назначаем заказ курьеру
        orderApiClient.acceptOrderApi(orderId,courierId);


    }
    @After
    public void tearDown() {
//      удаляем курьера
        courierApiClient.delete(courierId);

//      отменяем заказ (к сожалению, ручка /api/v1/orders/cancel не работает)
        ValidatableResponse orderCancel = orderApiClient.cancelOrderApi(new OrderCancel(orderTrack));
//      Выводим результат работы ручки /api/v1/orders/cancel в консоль
        System.out.println(orderCancel.extract().response().getBody().print());
    }


    @Test
    @DisplayName("Тест на проверку получения списка заказов по номеру курьера")
    @Description("Тест проверяет получение списка заказов по номеру курьера, проверят статус и что в теле возвращается не пустой список заказов.")
    public void orderGetListWithCourierId() {

//      Подготавливаем данные для вызова  GET request to /api/v1/orders/ c параметром  courierId
        HashMap<String, Integer> params = new HashMap <String, Integer>() {{put("courierId", courierId); }};

        ValidatableResponse orderGetListResponse = orderApiClient.getOrderListApiWithParametrs(params);

        int statusCode = orderGetListResponse.extract().statusCode();
        ArrayList<Integer> idFromOrders = orderGetListResponse.extract().path("orders.id");

        assertThat("Status code couldn't be 200", statusCode, equalTo(SC_OK));
        assertThat("OrderList is not get", idFromOrders.size(), is(not(0)));
    }
}
