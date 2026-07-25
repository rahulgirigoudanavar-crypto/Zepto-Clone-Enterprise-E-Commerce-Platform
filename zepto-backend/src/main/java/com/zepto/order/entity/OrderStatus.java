package com.zepto.order.entity;

public enum OrderStatus {

    PENDING_PAYMENT,

    PAYMENT_SUCCESS,

    PAYMENT_FAILED,

    CONFIRMED,

    PACKED,

    SHIPPED,

    OUT_FOR_DELIVERY,

    DELIVERED,

    CANCELLED

}