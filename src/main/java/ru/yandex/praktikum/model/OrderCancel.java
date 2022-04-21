package ru.yandex.praktikum.model;

public class OrderCancel {

    int orderTrack;

    public OrderCancel(int orderTrack) {
        this.orderTrack = orderTrack;
    }

    public int getOrderTrack() {
        return orderTrack;
    }

    public void setOrderTrack(int orderTrack) {
        this.orderTrack = orderTrack;
    }

    @Override
    public String toString() {
        return "OrderCancel{" +
                "orderTrack=" + orderTrack +
                '}';
    }
}


