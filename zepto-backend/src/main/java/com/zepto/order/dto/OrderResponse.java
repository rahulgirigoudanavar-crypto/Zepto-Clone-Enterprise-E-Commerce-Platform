package com.zepto.order.dto;

import java.math.BigDecimal;
import java.util.List;

import com.zepto.order.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;

    private String orderNumber;

    private Long userId;

    private OrderStatus orderStatus;

    private BigDecimal totalAmount;

    private String deliveryAddress;

    private String contactNumber;

    private List<OrderItemResponse> orderItems;

}