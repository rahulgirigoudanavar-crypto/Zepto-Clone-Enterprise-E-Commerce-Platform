package com.zepto.coupon.service;

import java.math.BigDecimal;
import java.util.List;

import com.zepto.coupon.dto.CouponRequest;
import com.zepto.coupon.dto.CouponResponse;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);

    CouponResponse updateCoupon(Long couponId, CouponRequest request);

    CouponResponse getCouponById(Long couponId);

    CouponResponse getCouponByCode(String code);

    List<CouponResponse> getAllCoupons();

    List<CouponResponse> getActiveCoupons();

    BigDecimal applyCoupon(String couponCode, BigDecimal orderAmount);

    void deleteCoupon(Long couponId);

}