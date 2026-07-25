package com.zepto.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zepto.coupon.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String code);

    boolean existsByCode(String code);

    List<Coupon> findByActiveTrue();

    List<Coupon> findByExpiryDateAfter(LocalDateTime currentDateTime);

    List<Coupon> findByActiveTrueAndExpiryDateAfter(LocalDateTime currentDateTime);

}