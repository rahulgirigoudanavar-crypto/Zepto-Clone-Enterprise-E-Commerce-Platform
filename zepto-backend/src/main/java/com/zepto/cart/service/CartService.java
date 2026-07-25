package com.zepto.cart.service;

import com.zepto.cart.dto.CartRequest;
import com.zepto.cart.dto.CartResponse;

public interface CartService {

    CartResponse addToCart(String userEmail, CartRequest request);

    CartResponse getCart(String userEmail);

    CartResponse updateCartItem(
            String userEmail,
            Long productId,
            Integer quantity);

    void removeFromCart(
            String userEmail,
            Long productId);

    void clearCart(String userEmail);
}