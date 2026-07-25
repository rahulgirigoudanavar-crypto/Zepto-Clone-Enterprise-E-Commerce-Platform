package com.zepto.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zepto.cart.entity.Cart;
import com.zepto.cart.entity.CartItem;
import com.zepto.cart.repository.CartRepository;
import com.zepto.inventory.entity.Inventory;
import com.zepto.inventory.repository.InventoryRepository;
import com.zepto.order.dto.OrderRequest;
import com.zepto.order.dto.OrderResponse;
import com.zepto.order.entity.Order;
import com.zepto.order.entity.OrderItem;
import com.zepto.order.entity.OrderStatus;
import com.zepto.order.repository.OrderItemRepository;
import com.zepto.order.repository.OrderRepository;
import com.zepto.user.entity.User;
import com.zepto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public OrderResponse placeOrder(OrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = Order.builder()
                .orderNumber("ORD-" + System.currentTimeMillis())
                .user(user)
                .orderStatus(OrderStatus.PENDING_PAYMENT)
                .deliveryAddress(request.getDeliveryAddress())
                .contactNumber(request.getContactNumber())
                .totalAmount(
                        cart.getCartItems()
                                .stream()
                                .map(CartItem::getTotalPrice)
                                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
                )
                .build();

        List<com.zepto.order.entity.OrderItem> orderItems = new java.util.ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {

            Inventory inventory = inventoryRepository.findByProduct(cartItem.getProduct())
                    .orElseThrow(() -> new RuntimeException(
                    "Inventory not found for product: "
                    + cartItem.getProduct().getName()));

            if (inventory.getTotalQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: "
                        + cartItem.getProduct().getName());
            }

            inventory.setTotalQuantity(inventory.getTotalQuantity() - cartItem.getQuantity());
            inventoryRepository.save(inventory);

            com.zepto.order.entity.OrderItem orderItem
                    = com.zepto.order.entity.OrderItem.builder()
                            .order(order)
                            .product(cartItem.getProduct())
                            .quantity(cartItem.getQuantity())
                            .price(cartItem.getPrice())
                            .totalPrice(cartItem.getTotalPrice())
                            .build();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order order) {

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .deliveryAddress(order.getDeliveryAddress())
                .contactNumber(order.getContactNumber())
                .orderItems(
                        order.getOrderItems()
                                .stream()
                                .map(orderItem -> com.zepto.order.dto.OrderItemResponse.builder()
                                .productId(orderItem.getProduct().getId())
                                .productName(orderItem.getProduct().getName())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getPrice())
                                .totalPrice(orderItem.getTotalPrice())
                                .build())
                                .toList()
                )
                .build();
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String orderStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            order.setOrderStatus(OrderStatus.valueOf(orderStatus.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid order status: " + orderStatus);
        }

        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponse(updatedOrder);
    }

    @Override
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered order cannot be cancelled");
        }

        for (OrderItem orderItem : order.getOrderItems()) {

            Inventory inventory = inventoryRepository.findByProduct(orderItem.getProduct())
                    .orElseThrow(() -> new RuntimeException(
                    "Inventory not found for product: "
                    + orderItem.getProduct().getName()));

            inventory.setTotalQuantity(inventory.getTotalQuantity() + orderItem.getQuantity());
            inventoryRepository.save(inventory);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
