package com.zepto.order.service;

import java.util.List;

import com.zepto.order.dto.OrderRequest;
import com.zepto.order.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);

    OrderResponse getOrderById(Long orderId);

    List<OrderResponse> getOrdersByUser(Long userId);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(Long orderId, String orderStatus);

    void cancelOrder(Long orderId);

}