package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.model.CourierCreate;
import ru.yandex.praktikum.model.CourierLogin;

import static io.restassured.RestAssured.given;

public class CourierApiClient extends BaseHttpClient {

    private  static  final  String COURIER_PATH = "/api/v1/courier/";

    @Step("Send POST request to /api/v1/courier/login : {courierLogin}")
    public ValidatableResponse login(CourierLogin courierLogin) {
        return given()
                .spec(baseSpec())
                .body(courierLogin)
                .when()
                .post(COURIER_PATH + "login")
                .then();
    }

    @Step("Send POST request to /api/v1/courier/login : {courierLoginConstructorWithoutSomeParameters}")
    public ValidatableResponse loginWithoutSomeParameters(String courierLoginConstructorWithoutSomeParameters) {
        return given()
                .spec(baseSpec())
                .body(courierLoginConstructorWithoutSomeParameters)
                .when()
                .post(COURIER_PATH + "login")
                .then();
    }

    @Step("Send POST request to /api/v1/courier : {courierCreate}")
    public ValidatableResponse create(CourierCreate courierCreate) {
        return given()
                .spec(baseSpec())
                .body(courierCreate)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Send POST request to /api/v1/courier : {courierCreateConstructorWithoutSomeParameters}")
    public ValidatableResponse createWithoutSomeParameters(String courierCreateConstructorWithoutSomeParameters) {
        return given()
                .spec(baseSpec())
                .body(courierCreateConstructorWithoutSomeParameters)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Send DELETE request to /api/v1/courier/:id - {courierId}")
    public ValidatableResponse delete(Integer courierId) {
        return given()
                .spec(baseSpec())
                .when()
                .delete(COURIER_PATH + courierId)
                .then();
    }

    @Step("Send DELETE request to /api/v1/courier/:id without id")
    public ValidatableResponse deleteDeleteWithoutId() {
        return given()
                .spec(baseSpec())
                .when()
                .delete(COURIER_PATH)
                .then();
    }
}
