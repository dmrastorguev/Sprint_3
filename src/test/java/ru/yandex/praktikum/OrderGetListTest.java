package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderApiClient;
import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;





public class OrderGetListTest {

    private OrderApiClient orderApiClient;

    @Before
    public void setUp() {
        orderApiClient = new OrderApiClient();
    }


    @Test
    @DisplayName("Тест на проверку получения списка заказов")
    @Description("Тест проверяет получение списка заказов без параметров, проверят статус и что в теле возвращается не пустой список заказов.")
    public void orderGetList() {

        ValidatableResponse orderGetListResponse = orderApiClient.getOrderListApi();

        int statusCode = orderGetListResponse.extract().statusCode();
        ArrayList<Integer> idFromOrders = orderGetListResponse.extract().path("orders.id");

        assertThat("Status code couldn't be 200", statusCode, equalTo(SC_OK));
        assertThat("OrderList is not get", idFromOrders.size(), is(not(0)));

    }
}

