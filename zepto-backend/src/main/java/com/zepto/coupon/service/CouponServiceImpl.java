package com.zepto.coupon.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zepto.coupon.dto.CouponRequest;
import com.zepto.coupon.dto.CouponResponse;
import com.zepto.coupon.entity.Coupon;
import com.zepto.coupon.entity.DiscountType;
import com.zepto.coupon.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public CouponResponse createCoupon(CouponRequest request) {

        if (couponRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Coupon code already exists");
        }

        Coupon coupon = Coupon.builder()
                .code(request.getCode().toUpperCase())
                .description(request.getDescription())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .minimumOrderAmount(request.getMinimumOrderAmount())
                .maximumDiscount(request.getMaximumDiscount())
                .expiryDate(request.getExpiryDate())
                .active(Boolean.TRUE.equals(request.getActive()))
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        return mapToResponse(savedCoupon);
    }

    @Override
    public CouponResponse updateCoupon(Long couponId,
            CouponRequest request) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(()
                        -> new RuntimeException("Coupon not found"));

        if (!coupon.getCode().equalsIgnoreCase(request.getCode())
                && couponRepository.existsByCode(request.getCode())) {

            throw new RuntimeException("Coupon code already exists");
        }

        coupon.setCode(request.getCode().toUpperCase());
        coupon.setDescription(request.getDescription());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinimumOrderAmount(request.getMinimumOrderAmount());
        coupon.setMaximumDiscount(request.getMaximumDiscount());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setActive(Boolean.TRUE.equals(request.getActive()));

        Coupon updatedCoupon = couponRepository.save(coupon);

        return mapToResponse(updatedCoupon);
    }

    @Override
    public CouponResponse getCouponById(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(()
                        -> new RuntimeException("Coupon not found"));

        return mapToResponse(coupon);
    }

    @Override
    public CouponResponse getCouponByCode(String code) {

        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(()
                        -> new RuntimeException("Coupon not found"));

        return mapToResponse(coupon);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {

        return couponRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CouponResponse> getActiveCoupons() {

        return couponRepository
                .findByActiveTrueAndExpiryDateAfter(LocalDateTime.now())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BigDecimal applyCoupon(String couponCode,
            BigDecimal orderAmount) {

        Coupon coupon = couponRepository.findByCode(couponCode.toUpperCase())
                .orElseThrow(()
                        -> new RuntimeException("Invalid coupon code"));

        if (!Boolean.TRUE.equals(coupon.getActive())) {
            throw new RuntimeException("Coupon is inactive");
        }

        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Coupon has expired");
        }

        if (orderAmount.compareTo(coupon.getMinimumOrderAmount()) < 0) {
            throw new RuntimeException(
                    "Minimum order amount should be ₹"
                    + coupon.getMinimumOrderAmount());
        }

        BigDecimal discount;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {

            discount = orderAmount
                    .multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));

            if (discount.compareTo(coupon.getMaximumDiscount()) > 0) {
                discount = coupon.getMaximumDiscount();
            }

        } else {

            discount = coupon.getDiscountValue();
        }

        return orderAmount.subtract(discount);
    }

    @Override
    public void deleteCoupon(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(()
                        -> new RuntimeException("Coupon not found"));

        coupon.setActive(false);

        couponRepository.save(coupon);
    }

    private CouponResponse mapToResponse(Coupon coupon) {

        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .description(coupon.getDescription())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .minimumOrderAmount(coupon.getMinimumOrderAmount())
                .maximumDiscount(coupon.getMaximumDiscount())
                .expiryDate(coupon.getExpiryDate())
                .active(coupon.getActive())
                .createdAt(coupon.getCreatedAt())
                .updatedAt(coupon.getUpdatedAt())
                .build();
    }
}
