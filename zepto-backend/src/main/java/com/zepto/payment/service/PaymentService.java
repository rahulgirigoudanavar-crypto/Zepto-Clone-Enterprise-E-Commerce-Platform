package com.zepto.payment.service;

import java.util.List;

import com.zepto.payment.dto.PaymentRequest;
import com.zepto.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPaymentOrder(PaymentRequest request);

    PaymentResponse verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByOrderId(Long orderId);

    List<PaymentResponse> getAllPayments();

}