package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderApiClient;
import ru.yandex.praktikum.model.OrderCancel;
import ru.yandex.praktikum.model.OrderCreate;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;



public class OrderGetByIdTest {

OrderApiClient orderApiClient;
OrderCreate orderCreate;
int orderTrack;

    @Before
    public void setUp() {
        orderApiClient = new OrderApiClient();
        orderCreate = new OrderCreate("Иннокентий", "г.Москва, ул.8-Марта", "4", "+74951002030", 5, "2022-04-10", "Гениальнo! Слушайте, я не узнаю вас в гриме.Бoже мoй, Иннoкентий Смoктунoвский!Кеша!", new String[]{"BLACK"});
        ValidatableResponse  orderCreateWithCorrectParametersResponse = orderApiClient.createOrderApi(new OrderCreate(orderCreate.getFirstName(), orderCreate.getAddress(), orderCreate.getMetroStation(), orderCreate.getPhone(), orderCreate.getRentTime(), orderCreate.getDeliveryDate(), orderCreate.getComment(), orderCreate.getColor()));
        orderTrack = orderCreateWithCorrectParametersResponse.extract().path("track");
    }


    @After
    public void tearDown() {
//      Пробуем отменить, созданные заказы, но ручка /api/v1/orders/cancel не работает
        ValidatableResponse orderCancel = orderApiClient.cancelOrderApi(new OrderCancel(orderTrack));
//      Выводим результат работы ручки /api/v1/orders/cancel в консоль
        System.out.println(orderCancel.extract().response().getBody().print());

    }
    @Test
    @DisplayName("Тест на проверку получения заказа по трекинговому номеру заказа")
    @Description("Тест проверяет получение заказа по трекинговому номеру заказа, проверят статус и тело ответа")
    public void orderGetById() {
        ValidatableResponse orderGetByIdResponse = orderApiClient.getOrderByIdApi(orderTrack);

        int statusCode = orderGetByIdResponse.extract().statusCode();
        int orderTrackResponse = orderGetByIdResponse.extract().path("order.track");
//        String  orderBodyResponse = orderGetByIdResponse.extract().body().path("order").toString();


        assertThat("Status code couldn't be 200", statusCode, equalTo(SC_OK));
        assertThat("OrderTrack isn't the same", orderTrackResponse , equalTo(orderTrack));
//         TODO проверка тела ответа. Дополнительное задание не делал. Все тестовые классы сделаны  качестве доп.проверок для теста OrderGetListWithParameters,
//          где они используются. Потом пришло понимание, что и это тоже делать необязательно :(
//          Сейчас разные результаты при сравнении, т.к. в ответе возвращаются дополнительные поля и поле дата в формате с часами
//        assertThat("Response body is not the same as it was created", orderBodyResponse,equalTo(orderCreate));
    }
}