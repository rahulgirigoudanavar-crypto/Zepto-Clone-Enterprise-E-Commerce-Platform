package com.zepto.payment.util;

import com.razorpay.Utils;

public class RazorpaySignatureUtil {

    private RazorpaySignatureUtil() {
    }

    public static boolean verifySignature(
            String orderId,
            String paymentId,
            String signature,
            String secret) {

        try {

            String payload = orderId + "|" + paymentId;

            return Utils.verifySignature(
                    payload,
                    signature,
                    secret);

        } catch (Exception ex) {
            return false;
        }
    }
}