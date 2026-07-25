package com.zepto.coupon.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zepto.coupon.dto.CouponRequest;
import com.zepto.coupon.dto.CouponResponse;
import com.zepto.coupon.service.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(
            @RequestBody CouponRequest request) {

        return new ResponseEntity<>(
                couponService.createCoupon(request),
                HttpStatus.CREATED);
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable Long couponId,
            @RequestBody CouponRequest request) {

        return ResponseEntity.ok(
                couponService.updateCoupon(couponId, request));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> getCouponById(
            @PathVariable Long couponId) {

        return ResponseEntity.ok(
                couponService.getCouponById(couponId));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponResponse> getCouponByCode(
            @PathVariable String code) {

        return ResponseEntity.ok(
                couponService.getCouponByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {

        return ResponseEntity.ok(
                couponService.getAllCoupons());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CouponResponse>> getActiveCoupons() {

        return ResponseEntity.ok(
                couponService.getActiveCoupons());
    }

    @GetMapping("/apply")
    public ResponseEntity<BigDecimal> applyCoupon(
            @RequestParam String couponCode,
            @RequestParam BigDecimal orderAmount) {

        return ResponseEntity.ok(
                couponService.applyCoupon(couponCode, orderAmount));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<String> deleteCoupon(
            @PathVariable Long couponId) {

        couponService.deleteCoupon(couponId);

        return ResponseEntity.ok("Coupon deleted successfully.");
    }
}