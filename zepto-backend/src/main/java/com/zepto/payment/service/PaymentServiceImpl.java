package com.zepto.payment.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.zepto.order.entity.Order;
import com.zepto.order.entity.OrderStatus;
import com.zepto.order.repository.OrderRepository;
import com.zepto.payment.dto.PaymentRequest;
import com.zepto.payment.dto.PaymentResponse;
import com.zepto.payment.entity.Payment;
import com.zepto.payment.entity.PaymentStatus;
import com.zepto.payment.repository.PaymentRepository;
import com.zepto.payment.util.RazorpaySignatureUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final RazorpayClient razorpayClient;

    @Override
    public PaymentResponse createPaymentOrder(PaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(()
                        -> new RuntimeException("Order not found"));

        if (paymentRepository.existsByOrder(order)) {
            throw new RuntimeException("Payment already exists for this order");
        }

        try {

            JSONObject options = new JSONObject();

            options.put("amount",
                    order.getTotalAmount()
                            .multiply(java.math.BigDecimal.valueOf(100))
                            .intValue());

            options.put("currency", "INR");

            options.put("receipt", order.getOrderNumber());

            com.razorpay.Order razorpayOrder
                    = razorpayClient.orders.create(options);

            Payment payment = Payment.builder()
                    .order(order)
                    .razorpayOrderId(razorpayOrder.get("id"))
                    .amount(order.getTotalAmount())
                    .paymentMethod(request.getPaymentMethod())
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            return mapToResponse(savedPayment);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }

    @Override
    public PaymentResponse verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(()
                        -> new RuntimeException("Payment not found"));

        boolean verified = RazorpaySignatureUtil.verifySignature(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature,
                razorpaySecret);

        if (!verified) {

            payment.setPaymentStatus(PaymentStatus.FAILED);

            paymentRepository.save(payment);

            throw new RuntimeException("Payment signature verification failed");
        }

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(razorpayPaymentId);
        payment.setPaidAt(java.time.LocalDateTime.now());

        Order order = payment.getOrder();
        order.setOrderStatus(OrderStatus.PAYMENT_SUCCESS);

        orderRepository.save(order);

        Payment updatedPayment = paymentRepository.save(payment);

        return mapToResponse(updatedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()
                        -> new RuntimeException("Payment not found"));

        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()
                        -> new RuntimeException("Order not found"));

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(()
                        -> new RuntimeException("Payment not found for this order"));

        return mapToResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .razorpayPaymentId(payment.getRazorpayPaymentId())
                .razorpaySignature(payment.getRazorpaySignature())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
