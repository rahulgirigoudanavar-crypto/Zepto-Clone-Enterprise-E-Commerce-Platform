package com.zepto.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zepto.cart.dto.CartItemResponse;
import com.zepto.cart.dto.CartRequest;
import com.zepto.cart.dto.CartResponse;
import com.zepto.cart.entity.Cart;
import com.zepto.cart.entity.CartItem;
import com.zepto.cart.repository.CartItemRepository;
import com.zepto.cart.repository.CartRepository;
import com.zepto.common.exception.ResourceNotFoundException;
import com.zepto.product.entity.Product;
import com.zepto.product.repository.ProductRepository;
import com.zepto.user.entity.User;
import com.zepto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse addToCart(String userEmail, CartRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity());

            cartItem.setTotalPrice(
                    cartItem.getPrice().multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())));

        } else {

            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .totalPrice(
                            product.getPrice().multiply(
                                    BigDecimal.valueOf(request.getQuantity())))
                    .build();
        }

        cartItemRepository.save(cartItem);

        return mapToCartResponse(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {

        List<CartItemResponse> items = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {

            CartItemResponse response = CartItemResponse.builder()
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .imageUrl(item.getProduct().getImageUrl())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getTotalPrice())
                    .build();

            items.add(response);

            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .totalItems(items.size())
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    public CartResponse getCart(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Cart not found"));

        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(
            String userEmail,
            Long productId,
            Integer quantity) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Cart item not found"));

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(
                cartItem.getPrice().multiply(BigDecimal.valueOf(quantity)));

        cartItemRepository.save(cartItem);

        return mapToCartResponse(cart);
    }

    @Override
    public void removeFromCart(
            String userEmail,
            Long productId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteAll(cart.getCartItems());
    }
}
