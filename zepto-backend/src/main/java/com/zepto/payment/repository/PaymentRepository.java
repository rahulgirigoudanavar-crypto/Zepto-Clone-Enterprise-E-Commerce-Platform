package com.zepto.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zepto.order.entity.Order;
import com.zepto.payment.entity.Payment;
import com.zepto.payment.entity.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder(Order order);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    Optional<Payment> findByTransactionId(String transactionId);

    boolean existsByOrder(Order order);

    long countByPaymentStatus(PaymentStatus paymentStatus);

}