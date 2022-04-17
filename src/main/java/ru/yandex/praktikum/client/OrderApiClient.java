package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.model.OrderCancel;
import ru.yandex.praktikum.model.OrderCreate;
import ru.yandex.praktikum.model.OrderFinish;

import java.util.HashMap;

import static io.restassured.RestAssured.given;



public class OrderApiClient extends BaseHttpClient {

    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Send POST request to /api/v1/orders : {orderCreate}")
    public ValidatableResponse createOrderApi(OrderCreate orderCreate) {
        return given()
                .spec(baseSpec())
                .body(orderCreate)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Send PUT request to /api/v1/orders/cancel : {orderCancel}")
    public ValidatableResponse cancelOrderApi(OrderCancel orderCancel) {
        return given()
                .spec(baseSpec())
                .body(orderCancel)
                .when()
                .put(ORDER_PATH + "/cancel")
                .then();
    }

    @Step("Send GET request to /api/v1/orders/track : {orderTrack}")
    public ValidatableResponse getOrderByIdApi(int orderTrack) {
        return given()
                 .spec(baseSpec())
                .queryParam("t", orderTrack)
                .when()
                .get(ORDER_PATH + "/track")
                .then();

    }

    @Step("Send PUT request to /api/v1/orders/accept/:id - orderId = {orderId} , courierId = {courierId}")
    public ValidatableResponse acceptOrderApi(int orderId,int courierId) {
        return given()
                 .spec(baseSpec())
                 .queryParam( "courierId", courierId)
                 .when()
                 .put(ORDER_PATH + "/accept/" + orderId)
                 .then();
}

    @Step("Send PUT request to /api/v1/orders/finish/:id : {orderFinish}")
    public ValidatableResponse finishOrderApi(OrderFinish orderFinish) {
        return given()
                .spec(baseSpec())
                .body(orderFinish)
                .when()
                .put(ORDER_PATH + "/finish")
                .then();
    }


    @Step("Send GET request to /api/v1/orders/ ")
    public ValidatableResponse getOrderListApi() {
        return given()
                .spec(baseSpec())
                .when()
                .get(ORDER_PATH )
                .then();
    }

    @Step("Send GET request to /api/v1/orders/ with {params}")
    public ValidatableResponse getOrderListApiWithParametrs(HashMap params) {
        return given()
                .spec(baseSpec())
                .queryParam(String.valueOf(params))
                .when()
                .get(ORDER_PATH )
                .then();
    }

}
